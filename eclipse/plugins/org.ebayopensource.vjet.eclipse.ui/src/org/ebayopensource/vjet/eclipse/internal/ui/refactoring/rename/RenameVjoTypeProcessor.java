/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.vjet.eclipse.internal.ui.refactoring.rename;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.dltk.mod.core.Flags;
import org.eclipse.dltk.mod.core.IModelElement;
import org.eclipse.dltk.mod.core.IScriptFolder;
import org.eclipse.dltk.mod.core.IScriptProject;
import org.eclipse.dltk.mod.core.ISourceModule;
import org.eclipse.dltk.mod.core.ISourceRange;
import org.eclipse.dltk.mod.core.IType;
import org.eclipse.dltk.mod.core.ModelException;
import org.eclipse.dltk.mod.core.ScriptModelUtil;
import org.eclipse.dltk.mod.core.search.IDLTKSearchConstants;
import org.eclipse.dltk.mod.core.search.IDLTKSearchScope;
import org.eclipse.dltk.mod.core.search.SearchMatch;
import org.eclipse.dltk.mod.internal.core.IJSModelElementInfo;
import org.eclipse.dltk.mod.internal.core.ModelManager;
import org.eclipse.dltk.mod.internal.core.util.VjoFlags;
import org.eclipse.dltk.mod.internal.corext.refactoring.Checks;
import org.eclipse.dltk.mod.internal.corext.refactoring.RefactoringAvailabilityTester;
import org.eclipse.dltk.mod.internal.corext.refactoring.RefactoringCoreMessages;
import org.eclipse.dltk.mod.internal.corext.refactoring.ScriptRefactoringArguments;
import org.eclipse.dltk.mod.internal.corext.refactoring.ScriptRefactoringDescriptor;
import org.eclipse.dltk.mod.internal.corext.refactoring.changes.DynamicValidationRefactoringChange;
import org.eclipse.dltk.mod.internal.corext.refactoring.changes.RenameResourceChange;
import org.eclipse.dltk.mod.internal.corext.refactoring.changes.RenameSourceModuleChange;
import org.eclipse.dltk.mod.internal.corext.refactoring.changes.TextChangeCompatibility;
/*
 * modify by patrick for support eclipse 3.5,
 * the original import is from org.eclipse.jdt.internal.corext.refactoring.code.ScriptableRefactoring(eclipse 3.3)
 */
import org.eclipse.dltk.mod.internal.corext.refactoring.code.ScriptableRefactoring;
import org.eclipse.dltk.mod.internal.corext.refactoring.participants.RenameTypeArguments;
import org.eclipse.dltk.mod.internal.corext.refactoring.participants.ScriptProcessors;
import org.eclipse.dltk.mod.internal.corext.refactoring.rename.RenameModifications;
import org.eclipse.dltk.mod.internal.corext.refactoring.rename.RenamingNameSuggestor;
import org.eclipse.dltk.mod.internal.corext.refactoring.rename.ScriptRenameProcessor;
import org.eclipse.dltk.mod.internal.corext.refactoring.tagging.IQualifiedNameUpdating;
import org.eclipse.dltk.mod.internal.corext.refactoring.tagging.IReferenceUpdating;
import org.eclipse.dltk.mod.internal.corext.refactoring.tagging.ISimilarDeclarationUpdating;
import org.eclipse.dltk.mod.internal.corext.refactoring.tagging.ITextUpdating;
import org.eclipse.dltk.mod.internal.corext.refactoring.util.ResourceUtil;
import org.eclipse.dltk.mod.internal.corext.refactoring.util.TextChangeManager;
import org.eclipse.dltk.mod.internal.corext.refactoring.vjet.IVjoRefactorings;
import org.eclipse.dltk.mod.internal.corext.refactoring.vjet.descriptors.RenameModelElementDescriptor;
import org.eclipse.dltk.mod.ui.DLTKUIPlugin;
import org.eclipse.dltk.mod.ui.ScriptElementLabels;
import org.eclipse.dltk.mod.ui.search.ElementQuerySpecification;
import org.eclipse.dltk.mod.ui.search.PatternQuerySpecification;
import org.eclipse.jdt.internal.corext.refactoring.JDTRefactoringDescriptorComment;
import org.eclipse.jdt.internal.corext.refactoring.util.QualifiedNameFinder;
import org.eclipse.jdt.internal.corext.refactoring.util.QualifiedNameSearchResult;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.GroupCategory;
import org.eclipse.ltk.core.refactoring.GroupCategorySet;
import org.eclipse.ltk.core.refactoring.IResourceMapper;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.IParticipantDescriptorFilter;
import org.eclipse.ltk.core.refactoring.participants.RefactoringArguments;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.ltk.core.refactoring.participants.RenameArguments;
import org.eclipse.text.edits.ReplaceEdit;

import org.ebayopensource.vjet.eclipse.core.IImportContainer;
import org.ebayopensource.vjet.eclipse.core.IImportDeclaration;
import org.ebayopensource.vjet.eclipse.core.IJSSourceModule;
import org.ebayopensource.vjet.eclipse.internal.ui.refactoring.core.RefactoringScopeFactory;
import org.ebayopensource.vjet.eclipse.internal.ui.refactoring.core.RefactoringSearchEngine;
import org.ebayopensource.vjet.eclipse.internal.ui.refactoring.core.SearchResultGroup;
import org.ebayopensource.vjet.eclipse.internal.ui.refactoring.util.VjoChecks;

public class RenameVjoTypeProcessor extends ScriptRenameProcessor implements ITextUpdating, IReferenceUpdating, IQualifiedNameUpdating,
		ISimilarDeclarationUpdating, IResourceMapper {

	private static final String ATTRIBUTE_QUALIFIED = "qualified"; //$NON-NLS-1$
	private static final String ATTRIBUTE_TEXTUAL_MATCHES = "textual"; //$NON-NLS-1$
	private static final String ATTRIBUTE_PATTERNS = "patterns"; //$NON-NLS-1$
	private static final String ATTRIBUTE_SIMILAR_DECLARATIONS = "similarDeclarations"; //$NON-NLS-1$
	private static final String ATTRIBUTE_MATCHING_STRATEGY = "matchStrategy"; //$NON-NLS-1$

	private static final GroupCategorySet CATEGORY_TYPE_RENAME = new GroupCategorySet(
			new GroupCategory(
					"org.eclipse.jdt.internal.corext.refactoring.rename.renameType.type", RefactoringCoreMessages.RenameTypeProcessor_changeCategory_type, RefactoringCoreMessages.RenameTypeProcessor_changeCategory_type_description)); //$NON-NLS-1$
	private static final GroupCategorySet CATEGORY_METHOD_RENAME = new GroupCategorySet(
			new GroupCategory(
					"org.eclipse.jdt.internal.corext.refactoring.rename.renameType.method", RefactoringCoreMessages.RenameTypeProcessor_changeCategory_method, RefactoringCoreMessages.RenameTypeProcessor_changeCategory_method_description)); //$NON-NLS-1$
	private static final GroupCategorySet CATEGORY_FIELD_RENAME = new GroupCategorySet(
			new GroupCategory(
					"org.eclipse.jdt.internal.corext.refactoring.rename.renameType.field", RefactoringCoreMessages.RenameTypeProcessor_changeCategory_fields, RefactoringCoreMessages.RenameTypeProcessor_changeCategory_fields_description)); //$NON-NLS-1$ 
	private static final GroupCategorySet CATEGORY_LOCAL_RENAME = new GroupCategorySet(
			new GroupCategory(
					"org.eclipse.jdt.internal.corext.refactoring.rename.renameType.local", RefactoringCoreMessages.RenameTypeProcessor_changeCategory_local_variables, RefactoringCoreMessages.RenameTypeProcessor_changeCategory_local_variables_description)); //$NON-NLS-1$			

	private IType fType;
	private SearchResultGroup[] fReferences;
	private TextChangeManager fChangeManager;
	private QualifiedNameSearchResult fQualifiedNameSearchResult;

	private boolean fUpdateReferences;

	private boolean fUpdateTextualMatches;

	private boolean fUpdateQualifiedNames;
	private String fFilePatterns;

	public static final String IDENTIFIER = "org.eclipse.jdt.ui.renameTypeProcessor"; //$NON-NLS-1$

	// --- similar elements

	private boolean fUpdateSimilarElements;
	private Map/* <IModelElement, String> */fFinalSimilarElementToName = null;
	private int fRenamingStrategy;

	// Preloaded information for the UI.
	private LinkedHashMap/* <IJavaElement, String> */fPreloadedElementToName = null;
	private Map/* <IJavaElement, Boolean> */fPreloadedElementToSelection = null;
	private LinkedHashMap/* <IJavaElement, String> */fPreloadedElementToNameDefault = null;

	// Cache information to decide whether to
	// re-update references and preload info
	private String fCachedNewName = null;
	private boolean fCachedRenameSimilarElements = false;
	private int fCachedRenamingStrategy = -1;
	private RefactoringStatus fCachedRefactoringStatus = null;

	public static final class ParticipantDescriptorFilter implements IParticipantDescriptorFilter {

		public boolean select(IConfigurationElement element, RefactoringStatus status) {
			IConfigurationElement[] params = element.getChildren(PARAM);
			for (int i = 0; i < params.length; i++) {
				IConfigurationElement param = params[i];
				if ("handlesSimilarDeclarations".equals(param.getAttribute(NAME)) && //$NON-NLS-1$
						"false".equals(param.getAttribute(VALUE))) { //$NON-NLS-1$
					return false;
				}
			}
			return true;
		}
	}

	private class NoOverrideProgressMonitor extends SubProgressMonitor {
		public NoOverrideProgressMonitor(IProgressMonitor monitor, int ticks) {
			super(monitor, ticks, SubProgressMonitor.SUPPRESS_SUBTASK_LABEL);
		}

		public void setTaskName(String name) {
			// do nothing
		}
	}

	/**
	 * Creates a new rename type processor.
	 * 
	 * @param type
	 *            the type, or <code>null</code> if invoked by scripting
	 */
	public RenameVjoTypeProcessor(IType type) {
		fType = type;
		if (type != null)
			setNewElementName(type.getElementName());
		fUpdateReferences = true; // default is yes
		fUpdateTextualMatches = false;
		fUpdateSimilarElements = false; // default is no
		fRenamingStrategy = RenamingNameSuggestor.STRATEGY_EXACT;
	}

	public IType getType() {
		return fType;
	}

	public String getIdentifier() {
		return IDENTIFIER;
	}

	public boolean isApplicable() throws CoreException {
		return RefactoringAvailabilityTester.isRenameAvailable(fType);
	}

	public String getProcessorName() {
		return RefactoringCoreMessages.RenameTypeRefactoring_name;
	}

	protected String[] getAffectedProjectNatures() throws CoreException {
		return ScriptProcessors.computeAffectedNatures(fType);
	}

	public Object[] getElements() {
		return new Object[] { fType };
	}

	protected RenameModifications computeRenameModifications() {
		RenameModifications result = new RenameModifications();
		result.rename(fType, new RenameTypeArguments(getNewElementName(), getUpdateReferences(), getUpdateSimilarDeclarations(), getSimilarElements()),
				createParticipantDescriptorFilter());

		if (isPrimaryType()) {
			ISourceModule cu = fType.getSourceModule();
			String newCUName = getNewCompilationUnit().getElementName();
			result.rename(cu, new RenameArguments(newCUName, getUpdateReferences()));
		}
		return result;
	}

	/*
	 * Note: this is a handle-only method!
	 */
	private boolean isPrimaryType() {
		String cuName = fType.getSourceModule().getElementName();
		String typeName = fType.getElementName();
		return Checks.isTopLevel(fType) && (typeName).equals(removeExtension(cuName));
	}

	private String removeExtension(String cuName) {
		if (cuName == null)
			return "";
		else
			return cuName.substring(0, cuName.lastIndexOf(".js"));
	}

	// ---- IRenameProcessor ----------------------------------------------

	public String getCurrentElementName() {
		return fType.getElementName();
	}

	public String getCurrentElementQualifier() {
		return ScriptModelUtil.getTypeContainerName(fType);
	}

	public RefactoringStatus checkNewElementName(String newName) {
		Assert.isNotNull(newName, "new name"); //$NON-NLS-1$
		RefactoringStatus result = Checks.checkTypeName(newName);
		if (Checks.isAlreadyNamed(fType, newName))
			result.addFatalError(RefactoringCoreMessages.RenameTypeRefactoring_choose_another_name);
		return result;
	}

	public Object getNewElement() {
		if (Checks.isTopLevel(fType)) {
			return getNewCompilationUnit().getType(getNewElementName());
		} else {
			return fType.getDeclaringType().getType(getNewElementName());
		}
	}

	private ISourceModule getNewCompilationUnit() {
		ISourceModule cu = fType.getSourceModule();
		if (isPrimaryType()) {
			IScriptFolder parent = fType.getScriptFolder();
			String renamedCUName = ScriptModelUtil.getRenamedCUName(cu, getNewElementName());
			return parent.getSourceModule(renamedCUName);
		} else {
			return cu;
		}
	}

	// ---- JavaRenameProcessor -------------------------------------------

	protected RenameArguments createRenameArguments() {
		return new RenameTypeArguments(getNewElementName(), getUpdateReferences(), getUpdateSimilarDeclarations(), getSimilarElements());
	}

	protected IParticipantDescriptorFilter createParticipantDescriptorFilter() {
		if (!getUpdateSimilarDeclarations())
			return null;
		return new ParticipantDescriptorFilter();
	}

	protected IFile[] getChangedFiles() throws CoreException {
		List result = new ArrayList();
		result.addAll(Arrays.asList(ResourceUtil.getFiles(fChangeManager.getAllSourceModules())));
		if (fQualifiedNameSearchResult != null)
			result.addAll(Arrays.asList(fQualifiedNameSearchResult.getAllFiles()));
		if (willRenameCU())
			result.add(ResourceUtil.getFile(fType.getSourceModule()));
		return (IFile[]) result.toArray(new IFile[result.size()]);
	}

	// public int getSaveMode() {
	// return RefactoringSaveHelper.SAVE_NON_JAVA_UPDATES;
	// }

	// ---- ITextUpdating -------------------------------------------------

	public boolean canEnableTextUpdating() {
		return true;
	}

	public boolean getUpdateTextualMatches() {
		return fUpdateTextualMatches;
	}

	public void setUpdateTextualMatches(boolean update) {
		fUpdateTextualMatches = update;
	}

	// ---- IReferenceUpdating --------------------------------------

	public void setUpdateReferences(boolean update) {
		fUpdateReferences = update;
	}

	public boolean canEnableUpdateReferences() {
		return true;
	}

	public boolean getUpdateReferences() {
		return fUpdateReferences;
	}

	// ---- IQualifiedNameUpdating ----------------------------------

	public boolean canEnableQualifiedNameUpdating() {
		return !fType.getScriptFolder().isRootFolder() && !(fType.getParent() instanceof IType);
	}

	public boolean getUpdateQualifiedNames() {
		return fUpdateQualifiedNames;
	}

	public void setUpdateQualifiedNames(boolean update) {
		fUpdateQualifiedNames = update;
	}

	public String getFilePatterns() {
		return fFilePatterns;
	}

	public void setFilePatterns(String patterns) {
		Assert.isNotNull(patterns);
		fFilePatterns = patterns;
	}

	// ---- ISimilarDeclarationUpdating

	public boolean canEnableSimilarDeclarationUpdating() {

		IProduct product = Platform.getProduct();
		if (product != null) {
			String property = product.getProperty("org.eclipse.jdt.ui.refactoring.handlesSimilarDeclarations"); //$NON-NLS-1$
			if ("false".equalsIgnoreCase(property)) //$NON-NLS-1$
				return false;
		}

		return true;
	}

	public void setUpdateSimilarDeclarations(boolean update) {
		fUpdateSimilarElements = update;
	}

	public boolean getUpdateSimilarDeclarations() {
		return fUpdateSimilarElements;
	}

	public int getMatchStrategy() {
		return fRenamingStrategy;

	}

	public void setMatchStrategy(int selectedStrategy) {
		fRenamingStrategy = selectedStrategy;
	}

	/**
	 * @return the similar elements of the type, i.e. IFields, IMethods, and
	 *         ILocalVariables. Returns <code>null</code> iff similar
	 *         declaration updating is not requested.
	 */
	public IModelElement[] getSimilarElements() {
		if (fFinalSimilarElementToName == null)
			return null;
		Set keys = fFinalSimilarElementToName.keySet();
		return (IModelElement[]) keys.toArray(new IModelElement[keys.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	public IResource getRefactoredResource(IResource element) {
		if (element instanceof IFile) {
			if (Checks.isTopLevel(fType) && element.equals(fType.getResource()))
				return getNewCompilationUnit().getResource();
		}
		return element;
	}

	// /**
	// * {@inheritDoc}
	// */
	// public IModelElement getRefactoredJavaElement(IModelElement element) {
	// if (element instanceof ICompilationUnit) {
	// if (Checks.isTopLevel(fType)
	// && element.equals(fType.getCompilationUnit()))
	// return getNewCompilationUnit();
	// } else if (element instanceof IMember) {
	// final IType newType = (IType) getNewElement();
	// final RefactoringHandleTransplanter transplanter = new
	// RefactoringHandleTransplanter(
	// fType, newType, fFinalSimilarElementToName);
	// return transplanter.transplantHandle((IMember) element);
	// }
	// return element;
	// }

	public RefactoringStatus checkInitialConditions(IProgressMonitor pm) throws CoreException {
		IType primary = (IType) fType.getPrimaryElement();
		if (primary == null || !primary.exists()) {
			String message = MessageFormat.format(RefactoringCoreMessages.RenameTypeRefactoring_does_not_exist, new String[] {
					ScriptModelUtil.getFullyQualifiedName(fType), fType.getSourceModule().getElementName() });
			return RefactoringStatus.createFatalErrorStatus(message);
		}
		fType = primary;
		return Checks.checkIfCuBroken(fType);
	}

	protected RefactoringStatus doCheckFinalConditions(IProgressMonitor pm, CheckConditionsContext context) throws CoreException {
		Assert.isNotNull(fType, "type"); //$NON-NLS-1$
		Assert.isNotNull(getNewElementName(), "newName"); //$NON-NLS-1$
		RefactoringStatus result = new RefactoringStatus();

		int referenceSearchTicks = fUpdateReferences || fUpdateSimilarElements ? 15 : 0;
		int affectedCusTicks = fUpdateReferences || fUpdateSimilarElements ? 10 : 1;
		int similarElementTicks = fUpdateSimilarElements ? 85 : 0;
		int createChangeTicks = 5;
		int qualifiedNamesTicks = fUpdateQualifiedNames ? 50 : 0;

		try {
			pm.beginTask("", 12 + referenceSearchTicks + affectedCusTicks + similarElementTicks + createChangeTicks + qualifiedNamesTicks); //$NON-NLS-1$
			pm.setTaskName(RefactoringCoreMessages.RenameTypeRefactoring_checking);

			fChangeManager = new TextChangeManager(true);

			result.merge(checkNewElementName(getNewElementName()));
			if (result.hasFatalError())
				return result;
			result.merge(Checks.checkIfCuBroken(fType));
			if (result.hasFatalError())
				return result;
			pm.worked(1);

			result.merge(checkTypesInCompilationUnit());
			pm.worked(1);

			// result.merge(checkForMethodsWithConstructorNames());
			// pm.worked(1);

			result.merge(checkImportedTypes());
			pm.worked(1);

			if (Checks.isTopLevel(fType) && (VjoFlags.isPublic(fType)))
				result.merge(Checks.checkSourceModuleNewName(fType.getSourceModule(), getNewElementName()));
			pm.worked(1);

			if (isPrimaryType())
				result.merge(checkNewPathValidity());
			pm.worked(1);

			result.merge(checkEnclosingTypes());
			pm.worked(1);

			result.merge(checkEnclosedTypes());
			pm.worked(1);

			result.merge(checkTypesInPackage());
			pm.worked(1);

			result.merge(checkTypesImportedInCu());
			pm.worked(1);

			result.merge(Checks.checkForMainMethods(fType));
			pm.worked(1);

			// before doing any expensive analysis
			if (result.hasFatalError())
				return result;

			result.merge(analyseEnclosedTypes());
			pm.worked(1);
			// before doing _the really_ expensive analysis
			if (result.hasFatalError())
				return result;

			// Load references, including similarly named elements
			if (fUpdateReferences || fUpdateSimilarElements) {
				pm.setTaskName(RefactoringCoreMessages.RenameTypeRefactoring_searching);
				result.merge(initializeReferences(new SubProgressMonitor(pm, referenceSearchTicks)));
			} else {
				fReferences = new SearchResultGroup[0];
			}

			pm.setTaskName(RefactoringCoreMessages.RenameTypeRefactoring_checking);
			if (pm.isCanceled())
				throw new OperationCanceledException();

			if (fUpdateReferences || fUpdateSimilarElements) {
				result.merge(analyzeAffectedCompilationUnits(new SubProgressMonitor(pm, affectedCusTicks)));
			} else {
				Checks.checkCompileErrorsInAffectedFile(result, fType.getResource());
				pm.worked(affectedCusTicks);
			}

			if (result.hasFatalError())
				return result;

			// if (fUpdateSimilarElements) {
			// result.merge(initializeSimilarElementsRenameProcessors(
			// new SubProgressMonitor(pm, similarElementTicks),
			// context));
			// if (result.hasFatalError())
			// return result;
			// }

			createChanges(new SubProgressMonitor(pm, createChangeTicks));

			if (fUpdateQualifiedNames)
				computeQualifiedNameMatches(new SubProgressMonitor(pm, qualifiedNamesTicks));

			return result;
		} finally {
			pm.done();
		}
	}

	/**
	 * Initializes the references to the type and the similarly named elements.
	 * This method creates both the fReferences and the fPreloadedElementToName
	 * fields.
	 * 
	 * May be called from the UI.
	 * 
	 * @param monitor
	 * @return initialization status
	 * @throws ModelException
	 *             some fundamental error with the underlying model
	 * @throws OperationCanceledException
	 *             if user canceled the task
	 * 
	 */
	public RefactoringStatus initializeReferences(IProgressMonitor monitor) throws ModelException, OperationCanceledException {

		Assert.isNotNull(fType);
		Assert.isNotNull(getNewElementName());

		// Do not search again if the preconditions have not changed.
		// Search depends on the type, the new name, the similarly named
		// elements, and
		// the strategy

		if (fReferences != null && (getNewElementName().equals(fCachedNewName)) && (fCachedRenameSimilarElements == getUpdateSimilarDeclarations())
				&& (fCachedRenamingStrategy == fRenamingStrategy))
			return fCachedRefactoringStatus;

		fCachedNewName = getNewElementName();
		fCachedRenameSimilarElements = fUpdateSimilarElements;
		fCachedRenamingStrategy = fRenamingStrategy;
		fCachedRefactoringStatus = new RefactoringStatus();

		try {
			// SearchPattern pattern = SearchPattern.createPattern(fType,
			// IDLTKSearchConstants.REFERENCES);
			ElementQuerySpecification elementQuerySpecification = new ElementQuerySpecification(fType, IDLTKSearchConstants.REFERENCES, RefactoringScopeFactory
					.create(fType), "wait...");
			fReferences = RefactoringSearchEngine.search(elementQuerySpecification, new TypeOccurrenceCollector(fType), monitor, fCachedRefactoringStatus);
			fReferences = VjoChecks.excludeSourceModules(fReferences, fCachedRefactoringStatus);

			// fPreloadedElementToName = new LinkedHashMap();
			// fPreloadedElementToSelection = new HashMap();

			// final String unQualifiedTypeName = fType.getElementName();

			monitor.beginTask("", fReferences.length); //$NON-NLS-1$

			// if (getUpdateSimilarDeclarations()) {
			//
			// RenamingNameSuggestor sugg = new RenamingNameSuggestor(
			// fRenamingStrategy);
			//
			// for (int i = 0; i < fReferences.length; i++) {
			// final ICompilationUnit cu = fReferences[i]
			// .getCompilationUnit();
			// if (cu == null)
			// continue;
			//
			// final SearchMatch[] results = fReferences[i]
			// .getSearchResults();
			//
			// for (int j = 0; j < results.length; j++) {
			//
			// if (!(results[j] instanceof TypeReferenceMatch))
			// continue;
			//
			// final TypeReferenceMatch match = (TypeReferenceMatch) results[j];
			// final List matches = new ArrayList();
			//
			// if (match.getLocalElement() != null)
			// matches.add(match.getLocalElement());
			// else
			// matches.add(match.getElement());
			//
			// final IJavaElement[] others = match.getOtherElements();
			// if (others != null)
			// matches.addAll(Arrays.asList(others));
			//
			// for (Iterator iter = matches.iterator(); iter.hasNext();) {
			// final IJavaElement element = (IJavaElement) iter
			// .next();
			//
			// if (!(element instanceof IMethod)
			// && !(element instanceof IField)
			// && !(element instanceof ILocalVariable))
			// continue;
			//
			// if (!isInDeclaredType(match.getOffset(), element))
			// continue;
			//
			// if (element instanceof IField) {
			// final IField currentField = (IField) element;
			// final String newFieldName = sugg
			// .suggestNewFieldName(currentField
			// .getJavaProject(), currentField
			// .getElementName(), Flags
			// .isStatic(currentField
			// .getFlags()),
			// unQualifiedTypeName,
			// getNewElementName());
			//
			// if (newFieldName != null)
			// fPreloadedElementToName.put(currentField,
			// newFieldName);
			// }
			//
			// if (element instanceof IMethod) {
			// final IMethod currentMethod = (IMethod) element;
			// addMethodRename(unQualifiedTypeName, sugg,
			// currentMethod);
			// }
			//
			// if (element instanceof ILocalVariable) {
			// final ILocalVariable currentLocal = (ILocalVariable) element;
			// final boolean isParameter;
			//
			// if (JavaModelUtil.isParameter(currentLocal)) {
			// addMethodRename(unQualifiedTypeName, sugg,
			// (IMethod) currentLocal.getParent());
			// isParameter = true;
			// } else
			// isParameter = false;
			//
			// final String newLocalName = sugg
			// .suggestNewLocalName(currentLocal
			// .getJavaProject(), currentLocal
			// .getElementName(), isParameter,
			// unQualifiedTypeName,
			// getNewElementName());
			//
			// if (newLocalName != null)
			// fPreloadedElementToName.put(currentLocal,
			// newLocalName);
			// }
			// }
			// }
			// if (monitor.isCanceled())
			// throw new OperationCanceledException();
			// }
			// }

			// for (Iterator iter = fPreloadedElementToName.keySet().iterator();
			// iter
			// .hasNext();) {
			// IJavaElement element = (IJavaElement) iter.next();
			// fPreloadedElementToSelection.put(element, Boolean.TRUE);
			// }
			// fPreloadedElementToNameDefault = (LinkedHashMap)
			// fPreloadedElementToName
			// .clone();

		} catch (OperationCanceledException e) {
			fReferences = null;
			// fPreloadedElementToName = null;
			throw new OperationCanceledException();
		}
		return fCachedRefactoringStatus;
	}

//	/**
//	 * @param matchOffset
//	 * @param parentElement
//	 * @return true iff the given search match offset (must be a match of a type
//	 *         reference) lies before the element name of its enclosing java
//	 *         element, false if not. In other words: If this method returns
//	 *         true, the match is the declared type (or return type) of the
//	 *         enclosing element.
//	 * @throws JavaModelException
//	 * 
//	 */
	// private boolean isInDeclaredType(int matchOffset, IJavaElement
	// parentElement)
	// throws JavaModelException {
	// if (parentElement != null) {
	// int enclosingNameOffset = 0;
	// if (parentElement instanceof IMethod
	// || parentElement instanceof IField)
	// enclosingNameOffset = ((IMember) parentElement).getNameRange()
	// .getOffset();
	// else if (parentElement instanceof ILocalVariable)
	// enclosingNameOffset = ((ILocalVariable) parentElement)
	// .getNameRange().getOffset();
	//
	// return (matchOffset < enclosingNameOffset);
	// }
	// return false;
	// }
	// private void addMethodRename(final String unQualifiedTypeName,
	// RenamingNameSuggestor sugg, final IMethod currentMethod)
	// throws JavaModelException {
	// if (!currentMethod.isConstructor()) {
	// final String newMethodName = sugg.suggestNewMethodName(
	// currentMethod.getElementName(), unQualifiedTypeName,
	// getNewElementName());
	//
	// if (newMethodName != null)
	// fPreloadedElementToName.put(currentMethod, newMethodName);
	// }
	// }
	private RefactoringStatus checkNewPathValidity() {
		IContainer c = fType.getSourceModule().getResource().getParent();

		String notRename = RefactoringCoreMessages.RenameTypeRefactoring_will_not_rename;
		IStatus status = c.getWorkspace().validateName(getNewElementName(), IResource.FILE);
		if (status.getSeverity() == IStatus.ERROR)
			return RefactoringStatus.createWarningStatus(status.getMessage() + ". " + notRename); //$NON-NLS-1$

		status = c.getWorkspace().validatePath(createNewPath(getNewElementName()), IResource.FILE);
		if (status.getSeverity() == IStatus.ERROR)
			return RefactoringStatus.createWarningStatus(status.getMessage() + ". " + notRename); //$NON-NLS-1$

		return new RefactoringStatus();
	}

	private String createNewPath(String newName) {
		return fType.getSourceModule().getResource().getFullPath().removeLastSegments(1).append(newName).toString();
	}

	private RefactoringStatus checkTypesImportedInCu() throws CoreException {
		IImportDeclaration imp = getImportedType(fType.getSourceModule(), getNewElementName());

		if (imp == null)
			return null;

		String msg = MessageFormat.format(RefactoringCoreMessages.RenameTypeRefactoring_imported, new Object[] { getNewElementName(),
				fType.getSourceModule().getResource().getFullPath() });
		IModelElement grandParent = imp.getParent().getParent();
		if (grandParent instanceof ISourceModule)
			return RefactoringStatus.createErrorStatus(msg/*
															 * JavaStatusContext
															 * .create(imp)
															 */);

		return null;
	}

	private RefactoringStatus checkTypesInPackage() throws CoreException {
		IType type = Checks.findTypeInScriptFolder(fType.getScriptFolder(), getNewElementName());
		if (type == null || !type.exists())
			return null;
		String msg = MessageFormat.format(RefactoringCoreMessages.RenameTypeRefactoring_exists, new String[] { getNewElementName(),
				fType.getScriptFolder().getElementName() });
		return RefactoringStatus.createErrorStatus(msg/*
														 * , JavaStatusContext
														 * .create(type)
														 */);
	}

	private RefactoringStatus checkEnclosedTypes() throws CoreException {
		IType enclosedType = findEnclosedType(fType, getNewElementName());
		if (enclosedType == null)
			return null;
		String msg = MessageFormat.format(RefactoringCoreMessages.RenameTypeRefactoring_encloses, new String[] { ScriptModelUtil.getFullyQualifiedName(fType),
				getNewElementName() });
		return RefactoringStatus.createErrorStatus(msg/*
														 * , JavaStatusContext
														 * .create(enclosedType)
														 */);
	}

	private RefactoringStatus checkEnclosingTypes() {
		IType enclosingType = findEnclosingType(fType, getNewElementName());
		if (enclosingType == null)
			return null;

		String msg = MessageFormat.format(RefactoringCoreMessages.RenameTypeRefactoring_enclosed, new String[] { ScriptModelUtil.getFullyQualifiedName(fType),
				getNewElementName() });
		return RefactoringStatus.createErrorStatus(msg/*
														 * , JavaStatusContext
														 * .create(enclosingType)
														 */);
	}

	private static IType findEnclosedType(IType type, String newName) throws CoreException {
		IType[] enclosedTypes = type.getTypes();
		for (int i = 0; i < enclosedTypes.length; i++) {
			if (newName.equals(enclosedTypes[i].getElementName()) || findEnclosedType(enclosedTypes[i], newName) != null)
				return enclosedTypes[i];
		}
		return null;
	}

	private static IType findEnclosingType(IType type, String newName) {
		IType enclosing = type.getDeclaringType();
		while (enclosing != null) {
			if (newName.equals(enclosing.getElementName()))
				return enclosing;
			else
				enclosing = enclosing.getDeclaringType();
		}
		return null;
	}

	private static IImportDeclaration getImportedType(ISourceModule cu, String typeName) throws CoreException {
		IImportDeclaration[] imports = getImports((IJSSourceModule) cu);
		String dotTypeName = "." + typeName; //$NON-NLS-1$
		for (int i = 0; i < imports.length; i++) {
			if (imports[i].getElementName().endsWith(dotTypeName))
				return imports[i];
		}
		return null;
	}

	// private RefactoringStatus checkForMethodsWithConstructorNames()
	// throws CoreException {
	// IMethod[] methods = fType.getMethods();
	// for (int i = 0; i < methods.length; i++) {
	// if (methods[i].isConstructor())
	// continue;
	// RefactoringStatus check = Checks.checkIfConstructorName(methods[i],
	// methods[i].getElementName(), getNewElementName());
	// if (check != null)
	// return check;
	// }
	// return null;
	// }

	private static IImportDeclaration[] getImports(IJSSourceModule jSourceModule) throws ModelException {
		final IImportDeclaration[] NO_IMPORTS = new IImportDeclaration[0];
		IModelElement[] imports = NO_IMPORTS;
		List al = new ArrayList();
		IImportContainer container = jSourceModule.getImportContainer();
		ModelManager manager = ModelManager.getModelManager();
		Object info = manager.getInfo(container);
		if (info == null) {
			return NO_IMPORTS;
		} else {
			// imports = jSourceModule.getImportContainer()
			// .getChildren();
			imports = ((IJSModelElementInfo) info).getChildren();
		}

		for (int i = 0; i < imports.length; i++) {
			al.add((IImportDeclaration) imports[i]);
		}
		// int length = elements.length;
		// IImportDeclaration[] imports = new IImportDeclaration[length];
		// System.arraycopy(elements, 0, imports, 0, length);
		// return imports;

		return (IImportDeclaration[]) al.toArray(new IImportDeclaration[al.size()]);
	}

	private RefactoringStatus checkImportedTypes() throws CoreException {
		RefactoringStatus result = new RefactoringStatus();
		IModelElement[] imports = getImports((IJSSourceModule) fType.getSourceModule());
		for (int i = 0; i < imports.length; i++)
			analyzeImportDeclaration((IImportDeclaration) imports[i], result);
		return result;
	}

	private RefactoringStatus checkTypesInCompilationUnit() {
		RefactoringStatus result = new RefactoringStatus();
		if (!Checks.isTopLevel(fType)) { // the other case checked in
			// checkTypesInPackage
			IType siblingType = fType.getDeclaringType().getType(getNewElementName());
			if (siblingType.exists()) {
				String msg = MessageFormat.format(RefactoringCoreMessages.RenameTypeRefactoring_member_type_exists, new String[] { getNewElementName(),
						ScriptModelUtil.getFullyQualifiedName(fType.getDeclaringType()) });
				result.addError(msg/*
									 * , JavaStatusContext.create(siblingType)
									 */);
			}
		}
		return result;
	}

	private RefactoringStatus analyseEnclosedTypes() throws CoreException {
		final ISourceRange typeRange = fType.getSourceRange();
		final RefactoringStatus result = new RefactoringStatus();
		// CompilationUnit cuNode = new
		// RefactoringASTParser(AST.JLS3).parse(fType
		// .getCompilationUnit(), false);
		// cuNode.accept(new ASTVisitor() {
		//
		// public boolean visit(TypeDeclaration node) { // enums and
		// // annotations can't
		// // be local
		// if (node.getStartPosition() <= typeRange.getOffset())
		// return true;
		// if (node.getStartPosition() > typeRange.getOffset()
		// + typeRange.getLength())
		// return true;
		//
		// if (getNewElementName().equals(node.getName().getIdentifier())) {
		// RefactoringStatusContext context = JavaStatusContext
		// .create(fType.getCompilationUnit(), node);
		// String msg = null;
		// if (node.isLocalTypeDeclaration()) {
		// msg = MessageFormat
		// .format(
		// RefactoringCoreMessages.RenameTypeRefactoring_local_type,
		// new String[] {
		// JavaElementUtil
		// .createSignature(fType),
		// getNewElementName() });
		// } else if (node.isMemberTypeDeclaration()) {
		// msg = MessageFormat
		// .format(
		// RefactoringCoreMessages.RenameTypeRefactoring_member_type,
		// new String[] {
		// JavaElementUtil
		// .createSignature(fType),
		// getNewElementName() });
		// }
		// if (msg != null)
		// result.addError(msg, context);
		// }
		//
		// MethodDeclaration[] methods = node.getMethods();
		// for (int i = 0; i < methods.length; i++) {
		// if (Modifier.isNative(methods[i].getModifiers())) {
		// RefactoringStatusContext context = JavaStatusContext
		// .create(fType.getCompilationUnit(), methods[i]);
		// String msg = MessageFormat
		// .format(
		// RefactoringCoreMessages.RenameTypeRefactoring_enclosed_type_native,
		// node.getName().getIdentifier());
		// result.addWarning(msg, context);
		// }
		// }
		// return true;
		// }
		// });
		return result;
	}

	private static ISourceModule getCompilationUnit(IImportDeclaration imp) {
		return (ISourceModule) imp.getParent().getParent();
	}

	private void analyzeImportedTypes(IType[] types, RefactoringStatus result, IImportDeclaration imp) throws CoreException {
		for (int i = 0; i < types.length; i++) {
			// could this be a problem (same package imports)?
			if (VjoFlags.isPublic(types[i]) && types[i].getElementName().equals(getNewElementName())) {
				String msg = MessageFormat.format(RefactoringCoreMessages.RenameTypeRefactoring_name_conflict1, new Object[] {
						ScriptModelUtil.getFullyQualifiedName(types[i]), getFullPath(getCompilationUnit(imp)) });
				result.addError(msg/* , JavaStatusContext.create(imp) */);
			}
		}
	}

	private static IModelElement convertFromImportDeclaration(IImportDeclaration declaration) throws CoreException {
		if (declaration.isOnDemand()) {
			String packageName = declaration.getElementName().substring(0, declaration.getElementName().length() - 2);
			return ScriptModelUtil.findTypeContainer(declaration.getScriptProject(), packageName);
		} else
			return ScriptModelUtil.findTypeContainer(declaration.getScriptProject(), declaration.getElementName());
	}

	private void analyzeImportDeclaration(IImportDeclaration imp, RefactoringStatus result) throws CoreException {
		if (!imp.isOnDemand())
			return; // analyzed earlier

		IModelElement imported = convertFromImportDeclaration(imp);
		if (imported == null)
			return;

		if (imported instanceof IScriptFolder) {
			ISourceModule[] cus = ((IScriptFolder) imported).getSourceModules();
			for (int i = 0; i < cus.length; i++) {
				analyzeImportedTypes(cus[i].getTypes(), result, imp);
			}
		} else {
			// cast safe: see JavaModelUtility.convertFromImportDeclaration
			analyzeImportedTypes(((IType) imported).getTypes(), result, imp);
		}
	}

	/*
	 * Analyzes all compilation units in which type is referenced
	 */
	private RefactoringStatus analyzeAffectedCompilationUnits(IProgressMonitor pm) throws CoreException {
		RefactoringStatus result = new RefactoringStatus();

		result.merge(VjoChecks.checkCompileErrorsInAffectedFile(fReferences));

		pm.beginTask("", fReferences.length); //$NON-NLS-1$
		result.merge(checkConflictingTypes(pm));
		return result;
	}

	private RefactoringStatus checkConflictingTypes(IProgressMonitor pm) throws CoreException {
		RefactoringStatus result = new RefactoringStatus();
		// DLTKSearchScopeFactory.getInstance().createSearchScope(modelElements,
		// includeInterp, toolkit)
		IDLTKSearchScope scope = RefactoringScopeFactory.create(fType);
		// SearchPattern pattern = SearchPattern.createPattern(
		// getNewElementName(), IDLTKSearchConstants.TYPE,
		// IDLTKSearchConstants.ALL_OCCURRENCES,
		// SearchUtils.GENERICS_AGNOSTIC_MATCH_RULE, scope
		// .getLanguageToolkit());// / int searchFor, int limitTo,
		// // int matchRule
		// // IDLTKLanguageToolkit toolkit
		PatternQuerySpecification patternQuerySpecification = new PatternQuerySpecification(getNewElementName(), IDLTKSearchConstants.TYPE, true,
				IDLTKSearchConstants.ALL_OCCURRENCES, scope, "wait...");
		ISourceModule[] cusWithReferencesToConflictingTypes = RefactoringSearchEngine.findAffectedSourceModules(patternQuerySpecification, pm, result);
		if (cusWithReferencesToConflictingTypes.length == 0)
			return result;
		ISourceModule[] cusWithReferencesToRenamedType = getCus(fReferences);

		ISourceModule[] intersection = isIntersectionEmpty(cusWithReferencesToRenamedType, cusWithReferencesToConflictingTypes);
		if (intersection.length == 0)
			return result;

		for (int i = 0; i < intersection.length; i++) {
			// RefactoringStatusContext context = JavaStatusContext
			// .create(intersection[i]);
			String message = MessageFormat.format(RefactoringCoreMessages.RenameTypeRefactoring_another_type, new String[] { getNewElementName(),
					intersection[i].getElementName() });
			result.addError(message/* , context */);
		}
		return result;
	}

	private static ISourceModule[] isIntersectionEmpty(ISourceModule[] a1, ISourceModule[] a2) {
		Set set1 = new HashSet(Arrays.asList(a1));
		Set set2 = new HashSet(Arrays.asList(a2));
		set1.retainAll(set2);
		return (ISourceModule[]) set1.toArray(new ISourceModule[set1.size()]);
	}

	private static ISourceModule[] getCus(SearchResultGroup[] searchResultGroups) {
		List cus = new ArrayList(searchResultGroups.length);
		for (int i = 0; i < searchResultGroups.length; i++) {
			ISourceModule cu = searchResultGroups[i].getSourceModule();
			if (cu != null)
				cus.add(cu);
		}
		return (ISourceModule[]) cus.toArray(new ISourceModule[cus.size()]);
	}

	private static String getFullPath(ISourceModule cu) {
		Assert.isTrue(cu.exists());
		return cu.getResource().getFullPath().toString();
	}

	public Change createChange(IProgressMonitor monitor) throws CoreException {
		try {
			monitor.beginTask(RefactoringCoreMessages.RenameTypeRefactoring_creating_change, 4);
			String project = null;
			IScriptProject javaProject = fType.getScriptProject();
			if (javaProject != null)
				project = javaProject.getElementName();
			// int flags = JavaRefactoringDescriptor.JAR_MIGRATION
			// | JavaRefactoringDescriptor.JAR_REFACTORING
			// | RefactoringDescriptor.STRUCTURAL_CHANGE;

			int flags = RefactoringDescriptor.STRUCTURAL_CHANGE;

			try {
				if (!Flags.isPrivate(fType.getFlags()))
					flags |= RefactoringDescriptor.MULTI_CHANGE;

				// if (fType.isAnonymous() || fType.isLocal())
				// flags |= JavaRefactoringDescriptor.JAR_SOURCE_ATTACHMENT;
			} catch (ModelException exception) {
				DLTKUIPlugin.log(exception);
			}
			final String description = MessageFormat.format(RefactoringCoreMessages.RenameTypeProcessor_descriptor_description_short, new String[] { fType
					.getElementName() });
			final String header = MessageFormat.format(RefactoringCoreMessages.RenameTypeProcessor_descriptor_description, new String[] {
					ScriptElementLabels.getDefault().getElementLabel(fType, ScriptElementLabels.ALL_FULLY_QUALIFIED), getNewElementName() });
			final String comment = new JDTRefactoringDescriptorComment(project, this, header).asString();
			final RenameModelElementDescriptor descriptor2 = new RenameModelElementDescriptor("TODO");

			descriptor2.setProject(project);
			descriptor2.setDescription(description);
			descriptor2.setComment(comment);
			descriptor2.setFlags(flags);
			descriptor2.setModelElement(fType);
			descriptor2.setNewName(getNewElementName());
			descriptor2.setUpdateQualifiedNames(fUpdateQualifiedNames);
			descriptor2.setUpdateTextualOccurrences(fUpdateTextualMatches);
			descriptor2.setUpdateReferences(fUpdateReferences);

			if (fUpdateQualifiedNames && fFilePatterns != null && !"".equals(fFilePatterns)) //$NON-NLS-1$
				descriptor2.setFileNamePatterns(fFilePatterns);
			descriptor2.setUpdateSimilarDeclarations(fUpdateSimilarElements);
			descriptor2.setMatchStrategy(fRenamingStrategy);
			final DynamicValidationRefactoringChange result = new DynamicValidationRefactoringChange(descriptor2,
					RefactoringCoreMessages.RenameTypeProcessor_change_name);

			if (fChangeManager.containsChangesIn(fType.getSourceModule())) {
				TextChange textChange = fChangeManager.get(fType.getSourceModule());
				if (textChange instanceof TextFileChange) {
					((TextFileChange) textChange).setSaveMode(TextFileChange.FORCE_SAVE);
				}
			}
			result.addAll(fChangeManager.getAllChanges());
			if (willRenameCU()) {
				IResource resource = fType.getSourceModule().getResource();
				if (resource != null && resource.isLinked()) {
					String ext = resource.getFileExtension();
					String renamedResourceName;
					if (ext == null)
						renamedResourceName = getNewElementName();
					else
						renamedResourceName = getNewElementName() + '.' + ext;
					result.add(new RenameResourceChange(null, fType.getSourceModule().getResource(), renamedResourceName, comment));
				} else {
					String renamedCUName = ScriptModelUtil.getRenamedCUName(fType.getSourceModule(), getNewElementName());
					result.add(new RenameSourceModuleChange(descriptor2, fType.getSourceModule(), renamedCUName, comment));
				}
			}
			monitor.worked(1);
			return result;
		} finally {
			fChangeManager = null;
		}
	}

	public Change postCreateChange(Change[] participantChanges, IProgressMonitor pm) throws CoreException {
		if (fQualifiedNameSearchResult != null) {
			try {
				return fQualifiedNameSearchResult.getSingleChange(Checks.getModifiedFiles(participantChanges));
			} finally {
				fQualifiedNameSearchResult = null;
			}
		} else {
			return null;
		}
	}

	private boolean willRenameCU() throws CoreException {
		String name = removeExtension(fType.getSourceModule().getElementName());
		if (!(Checks.isTopLevel(fType) && name.equals(fType.getElementName())))
			return false;
		if (!checkNewPathValidity().isOK())
			return false;
		if (!Checks.checkSourceModuleNewName(fType.getSourceModule(), getNewElementName()).isOK())
			return false;
		return true;
	}

	private void createChanges(IProgressMonitor pm) throws CoreException {
		try {
			pm.beginTask("", 12); //$NON-NLS-1$
			pm.setTaskName(RefactoringCoreMessages.RenameTypeProcessor_creating_changes);

			if (fUpdateReferences)
				addReferenceUpdates(fChangeManager, new SubProgressMonitor(pm, 3));

			// Similar names updates have already been added.

			pm.worked(1);

			IResource resource = fType.getSourceModule().getResource();
			// if we have a linked resource then we don't use CU renaming
			// directly. So we have to update the code by ourselves.
			if ((resource != null && resource.isLinked()) || !willRenameCU()) {
				addTypeDeclarationUpdate(fChangeManager);
				pm.worked(1);

				// addConstructorRenames(fChangeManager);
				// pm.worked(1);
			} else {
				pm.worked(2);
			}

			if (fUpdateTextualMatches) {
				pm.subTask(RefactoringCoreMessages.RenameTypeRefactoring_searching_text);
				TextMatchUpdater.perform(new SubProgressMonitor(pm, 1), RefactoringScopeFactory.create(fType), this, fChangeManager, fReferences);
				// if (fUpdateSimilarElements)
				// addSimilarElementsTextualUpdates(fChangeManager,
				// new SubProgressMonitor(pm, 3));
			}

		} finally {
			pm.done();
		}
	}

	private void addTypeDeclarationUpdate(TextChangeManager manager) throws CoreException {
		String name = RefactoringCoreMessages.RenameTypeRefactoring_update;
		int typeNameLength = fType.getElementName().length();
		ISourceModule cu = fType.getSourceModule();
		TextChangeCompatibility.addTextEdit(manager.get(cu), name, new ReplaceEdit(fType.getNameRange().getOffset(), typeNameLength, getNewElementName()));
	}

	// private void addConstructorRenames(TextChangeManager manager)
	// throws CoreException {
	// ICompilationUnit cu = fType.getCompilationUnit();
	// IMethod[] methods = fType.getMethods();
	// int typeNameLength = fType.getElementName().length();
	// for (int i = 0; i < methods.length; i++) {
	// if (methods[i].isConstructor()) {
	// /*
	// * constructor declarations cannot be fully qualified so we can
	// * use simple replace here
	// *
	// * if (methods[i].getNameRange() == null), then it's a binary
	// * file so it's wrong anyway (checked as a precondition)
	// */
	// String name =
	// RefactoringCoreMessages.RenameTypeRefactoring_rename_constructor;
	// TextChangeCompatibility.addTextEdit(manager.get(cu), name,
	// new ReplaceEdit(methods[i].getNameRange().getOffset(),
	// typeNameLength, getNewElementName()));
	// }
	// }
	// }

	private void addReferenceUpdates(TextChangeManager manager, IProgressMonitor pm) {
		pm.beginTask("", fReferences.length); //$NON-NLS-1$
		for (int i = 0; i < fReferences.length; i++) {
			ISourceModule cu = fReferences[i].getSourceModule();
			if (cu == null)
				continue;

			String name = RefactoringCoreMessages.RenameTypeRefactoring_update_reference;
			SearchMatch[] results = fReferences[i].getSearchResults();

			for (int j = 0; j < results.length; j++) {
				SearchMatch match = results[j];
				ReplaceEdit replaceEdit = new ReplaceEdit(match.getOffset(), match.getLength(), getNewElementName());
				TextChangeCompatibility.addTextEdit(manager.get(cu), name, replaceEdit, CATEGORY_TYPE_RENAME);
			}
			pm.worked(1);
		}
	}

	private void computeQualifiedNameMatches(IProgressMonitor pm) throws CoreException {
		IScriptFolder fragment = fType.getScriptFolder();
		if (fQualifiedNameSearchResult == null)
			fQualifiedNameSearchResult = new QualifiedNameSearchResult();
		QualifiedNameFinder.process(fQualifiedNameSearchResult, fType.getFullyQualifiedName(), fragment.getElementName() + "." + getNewElementName(), //$NON-NLS-1$
				fFilePatterns, fType.getScriptProject().getProject(), pm);
	}

	public RefactoringStatus initialize(RefactoringArguments arguments) {
		
	//ScriptRefactoringArguments scriptRefactoringArguments=(ScriptRefactoringArguments)arguments;
	if (arguments instanceof ScriptRefactoringArguments) {
		final ScriptRefactoringArguments extended= (ScriptRefactoringArguments) arguments;
		final String handle= extended.getAttribute(ScriptRefactoringDescriptor.ATTRIBUTE_INPUT);
		if (handle != null) {
			final IModelElement element= ScriptRefactoringDescriptor.handleToElement(extended.getProject(), handle, false);
			if (element == null || !element.exists() || element.getElementType() != IModelElement.TYPE)
				return ScriptableRefactoring.createInputFatalStatus(element, getRefactoring().getName(), IVjoRefactorings.RENAME_TYPE);
			else
				fType= (IType) element;
		} else
			return RefactoringStatus.createFatalErrorStatus(Messages.format(RefactoringCoreMessages.InitializableRefactoring_argument_not_exist, ScriptRefactoringDescriptor.ATTRIBUTE_INPUT));
		final String name= extended.getAttribute(ScriptRefactoringDescriptor.ATTRIBUTE_NAME);
		if (name != null && !"".equals(name)) //$NON-NLS-1$
			setNewElementName(name);
		else
			setNewElementName(fType.getElementName());//set old name here
//		else
//			return RefactoringStatus.createFatalErrorStatus(Messages.format(RefactoringCoreMessages.InitializableRefactoring_argument_not_exist, ScriptRefactoringDescriptor.ATTRIBUTE_NAME));
		final String patterns= extended.getAttribute(ATTRIBUTE_PATTERNS);
		if (patterns != null && !"".equals(patterns)) //$NON-NLS-1$
			fFilePatterns= patterns;
		final String references= extended.getAttribute(ScriptRefactoringDescriptor.ATTRIBUTE_REFERENCES);
		if (references != null) {
			fUpdateReferences= Boolean.valueOf(references).booleanValue();
		} else
			return RefactoringStatus.createFatalErrorStatus(Messages.format(RefactoringCoreMessages.InitializableRefactoring_argument_not_exist, ScriptRefactoringDescriptor.ATTRIBUTE_REFERENCES));
		final String matches= extended.getAttribute(ATTRIBUTE_TEXTUAL_MATCHES);
		if (matches != null) {
			fUpdateTextualMatches= Boolean.valueOf(matches).booleanValue();
		} else
			return RefactoringStatus.createFatalErrorStatus(Messages.format(RefactoringCoreMessages.InitializableRefactoring_argument_not_exist, ATTRIBUTE_TEXTUAL_MATCHES));
		final String qualified= extended.getAttribute(ATTRIBUTE_QUALIFIED);
		if (qualified != null) {
			fUpdateQualifiedNames= Boolean.valueOf(qualified).booleanValue();
		} else
			return RefactoringStatus.createFatalErrorStatus(Messages.format(RefactoringCoreMessages.InitializableRefactoring_argument_not_exist, ATTRIBUTE_QUALIFIED));
		final String similarDeclarations= extended.getAttribute(ATTRIBUTE_SIMILAR_DECLARATIONS);
		if (similarDeclarations != null)
			fUpdateSimilarElements= Boolean.valueOf(similarDeclarations).booleanValue();
		else
			return RefactoringStatus.createFatalErrorStatus(Messages.format(RefactoringCoreMessages.InitializableRefactoring_argument_not_exist, ATTRIBUTE_SIMILAR_DECLARATIONS));
		final String similarDeclarationsMatchingStrategy= extended.getAttribute(ATTRIBUTE_MATCHING_STRATEGY);
		if (similarDeclarationsMatchingStrategy != null) {
			try {
				fRenamingStrategy= Integer.valueOf(similarDeclarationsMatchingStrategy).intValue();
			} catch (NumberFormatException e) {
				return RefactoringStatus.createFatalErrorStatus(Messages.format(RefactoringCoreMessages.InitializableRefactoring_illegal_argument, new String[] {similarDeclarationsMatchingStrategy, ATTRIBUTE_QUALIFIED}));
			}
		} else
			return RefactoringStatus.createFatalErrorStatus(Messages.format(RefactoringCoreMessages.InitializableRefactoring_argument_not_exist, ATTRIBUTE_MATCHING_STRATEGY));
	} else
		return RefactoringStatus.createFatalErrorStatus(RefactoringCoreMessages.InitializableRefactoring_inacceptable_arguments);
	return new RefactoringStatus();
		
	
		
		
	}

	// --------- Similar names

	/**
	 * Creates and initializes the refactoring processors for similarly named
	 * elements
	 * 
	 * @param progressMonitor
	 * @param context
	 * @return status
	 * @throws CoreException
	 */
	// private RefactoringStatus initializeSimilarElementsRenameProcessors(
	// IProgressMonitor progressMonitor, CheckConditionsContext context)
	// throws CoreException {
	//
	// Assert.isNotNull(fPreloadedElementToName);
	// Assert.isNotNull(fPreloadedElementToSelection);
	//
	// final RefactoringStatus status = new RefactoringStatus();
	// final Set handledTopLevelMethods = new HashSet();
	// final Set warnings = new HashSet();
	// final List processors = new ArrayList();
	// fFinalSimilarElementToName = new HashMap();
	//
	// CompilationUnit currentResolvedCU = null;
	// ICompilationUnit currentCU = null;
	//
	// int current = 0;
	// final int max = fPreloadedElementToName.size();
	//
	// progressMonitor.beginTask("", max * 3); //$NON-NLS-1$
	// progressMonitor
	// .setTaskName(RefactoringCoreMessages.RenameTypeProcessor_checking_similarly_named_declarations_refactoring_conditions);
	//
	// for (Iterator iter = fPreloadedElementToName.keySet().iterator(); iter
	// .hasNext();) {
	//
	// final IJavaElement element = (IJavaElement) iter.next();
	//
	// current++;
	// progressMonitor.worked(3);
	//
	// // not selected? -> skip
	// if (!((Boolean) (fPreloadedElementToSelection.get(element)))
	// .booleanValue())
	// continue;
	//
	// // already registered? (may happen with overridden methods) -> skip
	// if (fFinalSimilarElementToName.containsKey(element))
	// continue;
	//
	// // CompilationUnit changed? (note: fPreloadedElementToName is sorted
	// // by CompilationUnit)
	// ICompilationUnit newCU = (ICompilationUnit) element
	// .getAncestor(IJavaElement.COMPILATION_UNIT);
	//
	// if (!newCU.equals(currentCU)) {
	//
	// checkCUCompleteConditions(status, currentResolvedCU, currentCU,
	// processors);
	//
	// if (status.hasFatalError())
	// return status;
	//
	// // reset values
	// currentResolvedCU = null;
	// currentCU = newCU;
	// processors.clear();
	// }
	//
	// final String newName = (String) fPreloadedElementToName
	// .get(element);
	// RefactoringProcessor processor = null;
	//
	// if (element instanceof ILocalVariable) {
	// final ILocalVariable currentLocal = (ILocalVariable) element;
	//
	// if (currentResolvedCU == null)
	// currentResolvedCU = new RefactoringASTParser(AST.JLS3)
	// .parse(currentCU, true);
	//
	// processor = createLocalRenameProcessor(currentLocal, newName,
	// currentResolvedCU);
	//
	// // don't check for conflicting rename => is done by
	// // #checkCUCompleteConditions().
	//
	// if (status.hasFatalError())
	// return status;
	// fFinalSimilarElementToName.put(currentLocal, newName);
	// }
	// if (element instanceof IField) {
	// final IField currentField = (IField) element;
	// processor = createFieldRenameProcessor(currentField, newName);
	//
	// status.merge(checkForConflictingRename(currentField, newName));
	// if (status.hasFatalError())
	// return status;
	// fFinalSimilarElementToName.put(currentField, newName);
	// }
	// if (element instanceof IMethod) {
	// IMethod currentMethod = (IMethod) element;
	// if (MethodChecks.isVirtual(currentMethod)) {
	//
	// final IType declaringType = currentMethod
	// .getDeclaringType();
	// ITypeHierarchy hierarchy = null;
	// if (!declaringType.isInterface())
	// hierarchy = declaringType
	// .newTypeHierarchy(new NullProgressMonitor());
	//
	// final IMethod topmost = MethodChecks
	// .getTopmostMethod(currentMethod, hierarchy,
	// new NullProgressMonitor());
	// if (topmost != null)
	// currentMethod = topmost;
	// if (handledTopLevelMethods.contains(currentMethod))
	// continue;
	// handledTopLevelMethods.add(currentMethod);
	// final IMethod[] ripples = MethodChecks
	// .getOverriddenMethods(currentMethod,
	// new NullProgressMonitor());
	//
	// if (checkForWarnings(warnings, newName, ripples))
	// continue;
	//
	// status.merge(checkForConflictingRename(ripples, newName));
	// if (status.hasFatalError())
	// return status;
	//
	// processor = createVirtualMethodRenameProcessor(
	// currentMethod, newName, ripples, hierarchy);
	// fFinalSimilarElementToName.put(currentMethod, newName);
	// for (int i = 0; i < ripples.length; i++) {
	// fFinalSimilarElementToName.put(ripples[i], newName);
	// }
	// } else {
	//
	// status.merge(checkForConflictingRename(
	// new IMethod[] { currentMethod }, newName));
	// if (status.hasFatalError())
	// break;
	//
	// fFinalSimilarElementToName.put(currentMethod, newName);
	//
	// processor = createNonVirtualMethodRenameProcessor(
	// currentMethod, newName);
	// }
	// }
	//
	// progressMonitor
	// .subTask(Messages
	// .format(
	// RefactoringCoreMessages.RenameTypeProcessor_progress_current_total,
	// new Object[] { String.valueOf(current),
	// String.valueOf(max) }));
	//
	// status.merge(processor
	// .checkInitialConditions(new NoOverrideProgressMonitor(
	// progressMonitor, 1)));
	//
	// if (status.hasFatalError())
	// return status;
	//
	// status
	// .merge(processor.checkFinalConditions(
	// new NoOverrideProgressMonitor(progressMonitor, 1),
	// context));
	//
	// if (status.hasFatalError())
	// return status;
	//
	// processors.add(processor);
	//
	// progressMonitor.worked(1);
	//
	// if (progressMonitor.isCanceled())
	// throw new OperationCanceledException();
	// }
	//
	// // check last CU
	// checkCUCompleteConditions(status, currentResolvedCU, currentCU,
	// processors);
	//
	// status.merge(addWarnings(warnings));
	//
	// progressMonitor.done();
	// return status;
	// }
	// private void checkCUCompleteConditions(final RefactoringStatus status,
	// CompilationUnit currentResolvedCU, ICompilationUnit currentCU,
	// List processors) throws CoreException {
	//
	// // check local variable conditions
	// List locals = getProcessorsOfType(processors,
	// RenameLocalVariableProcessor.class);
	// if (!locals.isEmpty()) {
	// RenameAnalyzeUtil.LocalAnalyzePackage[] analyzePackages = new
	// RenameAnalyzeUtil.LocalAnalyzePackage[locals
	// .size()];
	// TextChangeManager manager = new TextChangeManager();
	// int current = 0;
	// TextChange textChange = manager.get(currentCU);
	// textChange.setKeepPreviewEdits(true);
	// for (Iterator iterator = locals.iterator(); iterator.hasNext();) {
	// RenameLocalVariableProcessor localProcessor =
	// (RenameLocalVariableProcessor) iterator
	// .next();
	// RenameAnalyzeUtil.LocalAnalyzePackage analyzePackage = localProcessor
	// .getLocalAnalyzePackage();
	// analyzePackages[current] = analyzePackage;
	// for (int i = 0; i < analyzePackage.fOccurenceEdits.length; i++) {
	// TextChangeCompatibility
	// .addTextEdit(
	// textChange,
	// "", analyzePackage.fOccurenceEdits[i], GroupCategorySet.NONE);
	// //$NON-NLS-1$
	// }
	// current++;
	// }
	// status.merge(RenameAnalyzeUtil.analyzeLocalRenames(analyzePackages,
	// textChange, currentResolvedCU, false));
	// }
	//
	// /*
	// * There is room for performance improvement here: One could move
	// * shadowing analyzes out of the field and method processors and perform
	// * it here, thus saving on working copy creation. Drawback is increased
	// * heap consumption.
	// */
	// }
	private List getProcessorsOfType(List processors, Class type) {
		List tmp = new ArrayList();
		for (Iterator iter = processors.iterator(); iter.hasNext();) {
			RefactoringProcessor element = (RefactoringProcessor) iter.next();
			if (element.getClass().equals(type))
				tmp.add(element);
		}
		return tmp;
	}

	public boolean hasSimilarElementsToRename() {
		return false;
	}

	public IModelElement getRefactoredModelElement(IModelElement element) {
		return fType;
	}

	// ------------------ Error checking -------------

	/**
	 * Checks whether one of the given methods, which will all be renamed to
	 * "newName", shares a type with another already registered method which is
	 * renamed to the same new name and shares the same parameters.
	 * 
	 * @param methods
	 * @param newName
	 * @return status
	 * 
	 * @see #checkForConflictingRename(IField, String)
	 */
	// private RefactoringStatus checkForConflictingRename(IMethod[] methods,
	// String newName) {
	// RefactoringStatus status = new RefactoringStatus();
	// for (Iterator iter = fFinalSimilarElementToName.keySet().iterator(); iter
	// .hasNext();) {
	// IJavaElement element = (IJavaElement) iter.next();
	// if (element instanceof IMethod) {
	// IMethod alreadyRegisteredMethod = (IMethod) element;
	// String alreadyRegisteredMethodName = (String) fFinalSimilarElementToName
	// .get(element);
	// for (int i = 0; i < methods.length; i++) {
	// IMethod method2 = methods[i];
	// if ((alreadyRegisteredMethodName.equals(newName))
	// && (method2.getDeclaringType()
	// .equals(alreadyRegisteredMethod
	// .getDeclaringType()))
	// && (sameParams(alreadyRegisteredMethod, method2))) {
	// String message = Messages
	// .format(
	// RefactoringCoreMessages.RenameTypeProcessor_cannot_rename_methods_same_new_name,
	// new String[] {
	// alreadyRegisteredMethod
	// .getElementName(),
	// method2.getElementName(),
	// alreadyRegisteredMethod
	// .getDeclaringType()
	// .getFullyQualifiedName(),
	// newName });
	// status.addFatalError(message);
	// return status;
	// }
	// }
	// }
	// }
	// return status;
	// }
	// private static boolean sameParams(IMethod method, IMethod method2) {
	//
	// if (method.getNumberOfParameters() != method2.getNumberOfParameters())
	// return false;
	//
	// String[] params = method.getParameterTypes();
	// String[] params2 = method2.getParameterTypes();
	//
	// for (int i = 0; i < params.length; i++) {
	// String t1 = Signature.getSimpleName(Signature.toString(params[i]));
	// String t2 = Signature.getSimpleName(Signature.toString(params2[i]));
	// if (!t1.equals(t2)) {
	// return false;
	// }
	// }
	// return true;
	// }
	/**
	 * If suffix matching is enabled, the refactoring may suggest two fields to
	 * have the same name which reside in the same type. Same thing may also
	 * happen if the user makes poor choices for the field names.
	 * 
	 * Consider: FooBarThing fFooBarThing; FooBarThing fBarThing;
	 * 
	 * Rename "FooBarThing" to "DifferentHunk". Suggestion for both fields is
	 * "fDifferentHunk" (and rightly so).
	 * 
	 * @param currentField
	 * @param newName
	 * @return status
	 */
	// private RefactoringStatus checkForConflictingRename(IField currentField,
	// String newName) {
	// RefactoringStatus status = new RefactoringStatus();
	// for (Iterator iter = fFinalSimilarElementToName.keySet().iterator(); iter
	// .hasNext();) {
	// IJavaElement element = (IJavaElement) iter.next();
	// if (element instanceof IField) {
	// IField alreadyRegisteredField = (IField) element;
	// String alreadyRegisteredFieldName = (String) fFinalSimilarElementToName
	// .get(element);
	// if (alreadyRegisteredFieldName.equals(newName)) {
	// if (alreadyRegisteredField.getDeclaringType().equals(
	// currentField.getDeclaringType())) {
	//
	// String message = Messages
	// .format(
	// RefactoringCoreMessages.RenameTypeProcessor_cannot_rename_fields_same_new_name,
	// new String[] {
	// alreadyRegisteredField
	// .getElementName(),
	// currentField.getElementName(),
	// alreadyRegisteredField
	// .getDeclaringType()
	// .getFullyQualifiedName(),
	// newName });
	// status.addFatalError(message);
	// return status;
	// }
	// }
	// }
	// }
	// return status;
	// }
	// private RefactoringStatus addWarnings(final Set warnings) {
	// RefactoringStatus status = new RefactoringStatus();
	//
	// // Remove deleted ripple methods from user selection and add warnings
	// for (Iterator iter = warnings.iterator(); iter.hasNext();) {
	// final Warning warning = (Warning) iter.next();
	// final IMethod[] elements = warning.getRipple();
	// if (warning.isSelectionWarning()) {
	// String message = Messages
	// .format(
	// RefactoringCoreMessages.RenameTypeProcessor_deselected_method_is_overridden,
	// new String[] {
	// JavaElementLabels.getElementLabel(
	// elements[0],
	// JavaElementLabels.ALL_DEFAULT),
	// JavaElementLabels.getElementLabel(
	// elements[0].getDeclaringType(),
	// JavaElementLabels.ALL_DEFAULT) });
	// status.addWarning(message);
	// }
	// if (warning.isNameWarning()) {
	// String message = Messages
	// .format(
	// RefactoringCoreMessages.RenameTypeProcessor_renamed_method_is_overridden,
	// new String[] {
	// JavaElementLabels.getElementLabel(
	// elements[0],
	// JavaElementLabels.ALL_DEFAULT),
	// JavaElementLabels.getElementLabel(
	// elements[0].getDeclaringType(),
	// JavaElementLabels.ALL_DEFAULT) });
	// status.addWarning(message);
	// }
	// for (int i = 0; i < elements.length; i++)
	// fPreloadedElementToSelection.put(elements[i], Boolean.FALSE);
	// }
	// return status;
	// }
	/*
	 * If one of the methods of this ripple was deselected or renamed by the
	 * user, deselect the whole chain and add warnings.
	 */
	// private boolean checkForWarnings(final Set warnings, final String
	// newName,
	// final IMethod[] ripples) {
	//
	// boolean addSelectionWarning = false;
	// boolean addNameWarning = false;
	// for (int i = 0; i < ripples.length; i++) {
	// String newNameOfRipple = (String) fPreloadedElementToName
	// .get(ripples[i]);
	// Boolean selected = (Boolean) fPreloadedElementToSelection
	// .get(ripples[i]);
	//
	// // selected may be null here due to supermethods like
	// // setSomeClass(Object class) (subsignature match)
	// // Don't add a warning.
	// if (selected == null)
	// continue;
	//
	// if (!selected.booleanValue())
	// addSelectionWarning = true;
	//
	// if (!newName.equals(newNameOfRipple))
	// addNameWarning = true;
	// }
	// if (addSelectionWarning || addNameWarning)
	// warnings.add(new Warning(ripples, addSelectionWarning,
	// addNameWarning));
	//
	// return (addSelectionWarning || addNameWarning);
	// }
	//
	// private class Warning {
	//
	// private IMethod[] fRipple;
	// private boolean fSelectionWarning;
	// private boolean fNameWarning;
	//
	// public Warning(IMethod[] ripple, boolean isSelectionWarning,
	// boolean isNameWarning) {
	// fRipple = ripple;
	// fSelectionWarning = isSelectionWarning;
	// fNameWarning = isNameWarning;
	// }
	//
	// public boolean isNameWarning() {
	// return fNameWarning;
	// }
	//
	// public IMethod[] getRipple() {
	// return fRipple;
	// }
	//
	// public boolean isSelectionWarning() {
	// return fSelectionWarning;
	// }
	// }
	// ----------------- Processor creation --------
	// private RenameMethodProcessor createVirtualMethodRenameProcessor(
	// IMethod currentMethod, String newMethodName, IMethod[] ripples,
	// ITypeHierarchy hierarchy) throws ModelException {
	// RenameMethodProcessor processor = new RenameVirtualMethodProcessor(
	// currentMethod, ripples, fChangeManager, hierarchy,
	// CATEGORY_METHOD_RENAME);
	// initMethodProcessor(processor, newMethodName);
	// return processor;
	// }
	//
	// private RenameMethodProcessor createNonVirtualMethodRenameProcessor(
	// IMethod currentMethod, String newMethodName) {
	// RenameMethodProcessor processor = new RenameNonVirtualMethodProcessor(
	// currentMethod, fChangeManager, CATEGORY_METHOD_RENAME);
	// initMethodProcessor(processor, newMethodName);
	// return processor;
	// }
	//
	// private void initMethodProcessor(RenameMethodProcessor processor,
	// String newMethodName) {
	// processor.setNewElementName(newMethodName);
	// processor.setUpdateReferences(getUpdateReferences());
	// }
	//
	// private RenameFieldProcessor createFieldRenameProcessor(final IField
	// field,
	// final String newName) {
	// final RenameFieldProcessor processor = new RenameFieldProcessor(field,
	// fChangeManager, CATEGORY_FIELD_RENAME);
	// processor.setNewElementName(newName);
	// processor.setRenameGetter(false);
	// processor.setRenameSetter(false);
	// processor.setUpdateReferences(getUpdateReferences());
	// processor.setUpdateTextualMatches(false);
	// return processor;
	// }
	//
	// private RenameLocalVariableProcessor createLocalRenameProcessor(
	// final ILocalVariable local, final String newName,
	// final CompilationUnit compilationUnit) {
	// final RenameLocalVariableProcessor processor = new
	// RenameLocalVariableProcessor(
	// local, fChangeManager, compilationUnit, CATEGORY_LOCAL_RENAME);
	// processor.setNewElementName(newName);
	// processor.setUpdateReferences(getUpdateReferences());
	// return processor;
	// }
	// ----------- Edit creation -----------
	/**
	 * Updates textual matches for fields.
	 * 
	 * Strategy for matching text matches: Match and replace all fully qualified
	 * field names, but non-qualified field names only iff there are no fields
	 * which have the same original, but a different new name. Don't add java
	 * references; duplicate edits may be created but do not matter.
	 * 
	 * @param manager
	 * @param monitor
	 * @throws CoreException
	 */
	// private void addSimilarElementsTextualUpdates(TextChangeManager manager,
	// IProgressMonitor monitor) throws CoreException {
	//
	// final Map simpleNames = new HashMap();
	// final List forbiddenSimpleNames = new ArrayList();
	//
	// for (Iterator iter = fFinalSimilarElementToName.keySet().iterator(); iter
	// .hasNext();) {
	// final IJavaElement element = (IJavaElement) iter.next();
	// if (element instanceof IField) {
	//
	// if (forbiddenSimpleNames.contains(element.getElementName()))
	// continue;
	//
	// final String registeredNewName = (String) simpleNames
	// .get(element.getElementName());
	// final String newNameToCheck = (String) fFinalSimilarElementToName
	// .get(element);
	// if (registeredNewName == null)
	// simpleNames.put(element.getElementName(), newNameToCheck);
	// else if (!registeredNewName.equals(newNameToCheck))
	// forbiddenSimpleNames.add(element.getElementName());
	// }
	// }
	//
	// for (Iterator iter = fFinalSimilarElementToName.keySet().iterator(); iter
	// .hasNext();) {
	// final IJavaElement element = (IJavaElement) iter.next();
	// if (element instanceof IField) {
	// final IField field = (IField) element;
	// final String newName = (String) fFinalSimilarElementToName
	// .get(field);
	// TextMatchUpdater.perform(monitor, RefactoringScopeFactory
	// .create(field), field.getElementName(), field
	// .getDeclaringType().getFullyQualifiedName(), newName,
	// manager, new SearchResultGroup[0], forbiddenSimpleNames
	// .contains(field.getElementName()));
	// }
	// }
	// }
	// ------ UI interaction
	/**
	 * @return the map of similarly named elements (IJavaElement -> String with
	 *         new name) This map is live. Callers may change the new names of
	 *         the elements; they may not change the key set.
	 */
	// public Map/* <IJavaElement, String> */getSimilarElementsToNewNames() {
	// return fPreloadedElementToName;
	// }
	//
	// /**
	// * @return the map of similarly named elements (IJavaElement -> Boolean if
	// * selected) This map is live. Callers may change the selection
	// * status of the elements; they may not change the key set.
	// */
	// public Map/* <IJavaElement, Boolean> */getSimilarElementsToSelection() {
	// return fPreloadedElementToSelection;
	// }
	/**
	 * Resets the element maps back to the original status. This affects the
	 * maps returned in {@link #getSimilarElementsToNewNames() } and
	 * {@link #getSimilarElementsToSelection() }. All new names are reset to
	 * the calculated ones and every element gets selected.
	 */
	// public void resetSelectedSimilarElements() {
	// Assert.isNotNull(fPreloadedElementToName);
	// for (Iterator iter = fPreloadedElementToNameDefault.keySet().iterator();
	// iter
	// .hasNext();) {
	// final IJavaElement element = (IJavaElement) iter.next();
	// fPreloadedElementToName.put(element, fPreloadedElementToNameDefault
	// .get(element));
	// fPreloadedElementToSelection.put(element, Boolean.TRUE);
	// }
	// }
	/**
	 * @return true iff the "update similarly named elements" flag is set AND
	 *         the search yielded some elements to be renamed.
	 */
	// public boolean hasSimilarElementsToRename() {
	// if (!fUpdateSimilarElements)
	// return false;
	// if (fPreloadedElementToName == null)
	// return false;
	// if (fPreloadedElementToName.size() == 0)
	// return false;
	// return true;
	// }
}
