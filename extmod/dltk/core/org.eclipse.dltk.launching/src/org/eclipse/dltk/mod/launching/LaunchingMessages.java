/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.mod.launching;

import org.eclipse.osgi.util.NLS;

// TODO: move to internal package
public class LaunchingMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.mod.launching.LaunchingMessages";//$NON-NLS-1$
	
	public static String AbstractInterpreterInstallType_correctingLocations;

	public static String AbstractInterpreterInstallType_errorResolvingDefaultLibraries;

	public static String AbstractInterpreterInstallType_failedToReadFromDiscoverScriptOutputStream;

	public static String AbstractInterpreterInstallType_failedToResolveLibraryLocationsForWith;

	public static String AbstractInterpreterInstallType_fetchingInterpreterLibraryLocations;

	public static String AbstractInterpreterInstallType_libraryDiscoveryScript;

	public static String AbstractInterpreterInstallType_nullResultFromProcess;

	public static String AbstractInterpreterInstallType_output;

	public static String AbstractInterpreterInstallType_problemWhileResolvingInterpreterLibraries;

	public static String AbstractInterpreterInstallType_resolvingLibraryPaths;

	public static String AbstractInterpreterInstallType_unableToLookupLibraryPaths;

	public static String AbstractInterpreterRunner_executionWasCancelled;

	public static String AbstractInterpreterRunner_launching;

	public static String AbstractInterpreterRunner_running;

	public static String AbstractScriptLaunchConfigurationDelegate_debuggingEngineNotSelected;

	public static String AbstractScriptLaunchConfigurationDelegate_executingRunner;

	public static String AbstractScriptLaunchConfigurationDelegate_generatingInterpreterConfiguration;

	public static String AbstractScriptLaunchConfigurationDelegate_gettingInterpreterRunner;

	public static String AbstractScriptLaunchConfigurationDelegate_InterpreterEnvironment_home_directory_for__0__does_not_exist___1__6;
	public static String AbstractScriptLaunchConfigurationDelegate_InterpreterEnvironment_home_directory_not_specified_for__0__5;
	public static String AbstractScriptLaunchConfigurationDelegate_Main_type_not_specified_11;

	public static String AbstractScriptLaunchConfigurationDelegate_startingLaunchConfiguration;
	public static String AbstractScriptLaunchConfigurationDelegate_The_specified_InterpreterEnvironment_installation_does_not_exist_4;

	public static String AbstractScriptLaunchConfigurationDelegate_validatingLaunchConfiguration;
	public static String AbstractScriptLaunchConfigurationDelegate_Working_directory_does_not_exist___0__12;

	public static String DefaultProjectBuildpathEntry_2;
	public static String DefaultProjectBuildpathEntry_3;
	public static String DefaultProjectBuildpathEntry_4;

	public static String DLTKLaunchingPlugin_rebindInterpreters;

	public static String errDebuggingEngineScriptFileDoesntExist;

	public static String errDebuggingEngineWorkingDirectoryDoesntExist;

	public static String errNoInterpreterExecutablesFound;

	public static String errNonExistentOrInvalidInstallLocation;

	public static String InterpreterRunnerDoesntExist;

	public static String ScriptRuntime_badFormat;
	public static String ScriptRuntime_exceptionsOccurred;
	public static String ScriptRuntime_Launch_configuration__0__references_non_existing_project__1___1;
	public static String ScriptRuntime_Could_not_resolve_classpath_container___0__1;
	public static String ScriptRuntime_Buildpath_references_non_existant_archive___0__4;
	public static String ScriptRuntime_26;
	public static String ScriptRuntime_27;

	public static String ScriptRuntime_28;
	public static String ScriptRuntime_31;
	public static String ScriptRuntime_32;
	
	public static String LaunchingPlugin_32;
	public static String LaunchingPlugin_33;
	public static String LaunchingPlugin_34;
	public static String LaunchingPlugin_0;
	public static String LaunchingPlugin_1;

	
	public static String InterpreterInstallType_duplicateInterpreter;

	
	public static String RuntimeBuildpathEntry_An_exception_occurred_generating_runtime_classpath_memento_8;
	public static String RuntimeBuildpathEntry_Unable_to_recover_runtime_class_path_entry_type_2;
	public static String RuntimeBuildpathEntry_Unable_to_recover_runtime_class_path_entry_location_3;
	public static String RuntimeBuildpathEntry_Unable_to_recover_runtime_class_path_entry___missing_project_name_4;
	public static String RuntimeBuildpathEntry_Unable_to_recover_runtime_class_path_entry___missing_archive_path_5;
	public static String RuntimeBuildpathEntry_Unable_to_recover_runtime_class_path_entry___missing_variable_name_6;
	public static String RuntimeBuildpathEntry_Illegal_classpath_entry__0__1;

	public static String InterpreterEnvironmentContainer_InterpreterEnvironment_System_Library_1;

	public static String InterpreterEnvironmentContainerInitializer_InterpreterEnvironment_referenced_by_classpath_container__0__does_not_exist__1;
	public static String InterpreterEnvironmentContainerInitializer_Buildpath_entry__0__does_not_refer_to_an_existing_library__2;
	public static String InterpreterEnvironmentContainerInitializer_Buildpath_entry__0__does_not_refer_to_a_library__3;
	public static String InterpreterEnvironmentContainerInitializer_Default_System_Library_1;

	
	public static String AbstractInterpreterRunner_0;
	public static String AbstractScriptLaunchConfigurationDelegate_20;

	
	static {
		NLS.initializeMessages(BUNDLE_NAME, LaunchingMessages.class);
	}

	public static String StandardInterpreterRunner;
	public static String StandardInterpreterRunner_Unable_to_locate_executable_for;
	
	public static String NoDefaultInterpreterStatusHandler_title;
	public static String NoDefaultInterpreterStatusHandler_message;
	
	public static String ScriptRuntime_notDefaultInterpreter;
	
	public static String ScriptLaunchShortcut_0;
	public static String ScriptLaunchShortcut_1;
	public static String ScriptLaunchShortcut_3;	
	public static String ScriptLaunchShortcut_Choose_a_main_script_to_launch;

	public static String LaunchShortcut_selectScriptToLaunch;
	public static String LaunchShortcut_selectionContainsNoScript;
	public static String LaunchShortcut_searchingForScripts;	
	
	public static String ScriptProjectSourceContainerTypeDelegate_5;
	public static String ScriptProjectSourceContainerTypeDelegate_6;
	public static String ScriptProjectSourceContainerTypeDelegate_7;

	public static String ScriptRuntime_Buildpath_references_non_existant_project___0__3;
	
}
