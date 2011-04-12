package vjo.security;

/*
 * @(#)src/classes/sov/java/security/SecureClassLoader.java, security, asdev, 20070119 1.14
 * ===========================================================================
 * Licensed Materials - Property of IBM
 * "Restricted Materials of IBM"
 *
 * IBM SDK, Java(tm) 2 Technology Edition, v5.0
 * (C) Copyright IBM Corp. 1998, 2005. All Rights Reserved
 * ===========================================================================
 */

/*
 * ===========================================================================
 (C) Copyright Sun Microsystems Inc, 1992, 2004. All rights reserved.
 * ===========================================================================
 */



/* 
 *===========================================================================
 * Change activity:
 *
 * Reason  Date   Origin   Description
 * ------  ------ -------- --------------------------------------------------- 
 * 099353  100106 preece   RTSJ: disable ProtectionDomain cache for real-time
 *                        
 *===========================================================================
 */
 
import vjo.lang.* ;
import vjo.lang.SecurityManager ;
import vjo.lang.System ;

import vjo.util.HashMap;
import vjo.util.ArrayList;

//import java.net.URL;
//
//import com.ibm.jvm.Util;                                        /*ibm@99353*/ 
//
//import sun.security.util.Debug;

/** 
 * This class extends ClassLoader with additional support for defining
 * classes with an associated code source and permissions which are
 * retrieved by the system policy by default.
 *
 * @version 1.85, 05/05/04
 * @author  Li Gong 
 * @author  Roland Schemers
 */
public class SecureClassLoader extends ClassLoader {
    /*
     * If initialization succeed this is set to true and security checks will
     * succeed. Otherwise the object is not initialized and the object is
     * useless.
     */
    private boolean initialized = false;

    // HashMap that maps CodeSource to ProtectionDomain
    private HashMap pdcache = new HashMap(11);

    private static final Debug debug = Debug.getInstance("scl");

    /**
     * Creates a new SecureClassLoader using the specified parent
     * class loader for delegation.
     *
     * <p>If there is a security manager, this method first
     * calls the security manager's <code>checkCreateClassLoader</code> 
     * method  to ensure creation of a class loader is allowed.
     * <p>
     * @param parent the parent ClassLoader
     * @exception  SecurityException  if a security manager exists and its  
     *             <code>checkCreateClassLoader</code> method doesn't allow 
     *             creation of a class loader.
     * @see SecurityManager#checkCreateClassLoader
     */
    protected SecureClassLoader(ClassLoader parent) {
        super(parent);
        // this is to make the stack depth consistent with 1.1
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkCreateClassLoader();
        }
        initialized = true;
    }

    /**
     * Creates a new SecureClassLoader using the default parent class
     * loader for delegation.
     *
     * <p>If there is a security manager, this method first
     * calls the security manager's <code>checkCreateClassLoader</code> 
     * method  to ensure creation of a class loader is allowed.
     *
     * @exception  SecurityException  if a security manager exists and its  
     *             <code>checkCreateClassLoader</code> method doesn't allow 
     *             creation of a class loader.
     * @see SecurityManager#checkCreateClassLoader
     */
    protected SecureClassLoader() {
        super();
        // this is to make the stack depth consistent with 1.1
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkCreateClassLoader();
        }
        initialized = true;
    }

    /**
     * Converts an array of bytes into an instance of class Class,
     * with an optional CodeSource. Before the
     * class can be used it must be resolved.
     * <p>
     * If a non-null CodeSource is supplied a ProtectionDomain is
     * constructed and associated with the class being defined.
     * <p>
     * @param      name the expected name of the class, or <code>null</code>
     *                  if not known, using '.' and not '/' as the separator
     *                  and without a trailing ".class" suffix.
     * @param      b    the bytes that make up the class data. The bytes in 
     *             positions <code>off</code> through <code>off+len-1</code> 
     *             should have the format of a valid class file as defined 
     *             by the 
     *             <a href="http://java.sun.com/docs/books/vmspec/">Java 
     *             Virtual Machine Specification</a>.
     * @param      off  the start offset in <code>b</code> of the class data
     * @param      len  the length of the class data
     * @param      cs   the associated CodeSource, or <code>null</code> if none
     * @return the <code>Class</code> object created from the data,
     *         and optional CodeSource.
     * @exception  ClassFormatError if the data did not contain a valid class
     * @exception  IndexOutOfBoundsException if either <code>off</code> or 
     *             <code>len</code> is negative, or if 
     *             <code>off+len</code> is greater than <code>b.length</code>.
     *
     * @exception  SecurityException if an attempt is made to add this class
     *             to a package that contains classes that were signed by
     *             a different set of certificates than this class, or if 
     *             the class name begins with "java.".
     */
    protected final Class<?> defineClass(String name,
                                         byte[] b, int off, int len,
                                         CodeSource cs)
    {
        if (cs == null)
            return defineClass(name, b, off, len);
        else 
            return defineClass(name, b, off, len, getProtectionDomain(cs));
    }

    /**
     * Converts a {@link java.nio.ByteBuffer <tt>ByteBuffer</tt>}
     * into an instance of class <tt>Class</tt>, with an optional CodeSource. 
     * Before the class can be used it must be resolved.
     * <p>
     * If a non-null CodeSource is supplied a ProtectionDomain is
     * constructed and associated with the class being defined.
     * <p>
     * @param      name the expected name of the class, or <code>null</code>
     *                  if not known, using '.' and not '/' as the separator
     *                  and without a trailing ".class" suffix.
     * @param      b    the bytes that make up the class data.  The bytes from positions
     *                  <tt>b.position()</tt> through <tt>b.position() + b.limit() -1</tt>
     *                  should have the format of a valid class file as defined by the
     *                  <a href="http://java.sun.com/docs/books/vmspec/">Java Virtual
     *                  Machine Specification</a>.
     * @param      cs   the associated CodeSource, or <code>null</code> if none
     * @return the <code>Class</code> object created from the data,
     *         and optional CodeSource.
     * @exception  ClassFormatError if the data did not contain a valid class
     * @exception  SecurityException if an attempt is made to add this class
     *             to a package that contains classes that were signed by
     *             a different set of certificates than this class, or if 
     *             the class name begins with "java.".
     *
     * @since  1.5
     */
    protected final Class<?> defineClass(String name, java.nio.ByteBuffer b,
                                         CodeSource cs)
    {
        if (cs == null)
            return defineClass(name, b, (ProtectionDomain)null);
        else 
            return defineClass(name, b, getProtectionDomain(cs));
    }

    /**
     * Returns the permissions for the given CodeSource object.
     * <p>
     * This method is invoked by the defineClass method which takes
     * a CodeSource as an argument when it is constructing the
     * ProtectionDomain for the class being defined.
     * <p>
     * @param codesource the codesource.
     *
     * @return the permissions granted to the codesource.
     *
     */
    protected PermissionCollection getPermissions(CodeSource codesource)
    {
        check();
        return new Permissions(); // ProtectionDomain defers the binding
    }

    /*
     * Returned cached ProtectionDomain for the specified CodeSource.
     */
    private ProtectionDomain getProtectionDomain(CodeSource cs) {
        if (cs == null)
            return null;

        ProtectionDomain pd = null;
        synchronized (pdcache) {
            if ( ! Util.isRealTime()) {                         /*ibm@99353*/       
                pd = (ProtectionDomain)pdcache.get(cs);
            }                                                   /*ibm@99353*/
            if (pd == null) {
                PermissionCollection perms = getPermissions(cs);
                pd = new ProtectionDomain(cs, perms, this, null);
                if (pd != null) {
                    if ( ! Util.isRealTime()) {                 /*ibm@99353*/
                        pdcache.put(cs, pd);
                    }                                           /*ibm@99353*/
                    if (debug != null) {
                        debug.println(" getPermissions "+ pd);
                        debug.println("");
                    }
                }
            }
        }
        return pd;
    }

    /*
     * Check to make sure the class loader has been initialized.
     */
    private void check() { 
        if (!initialized) {
            throw new SecurityException("ClassLoader object not initialized");
        }
    }

}

