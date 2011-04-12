package vjo.net;

/*
 * @(#)src/classes/sov/java/net/ConnectException.java, net, asdev, 20070119 1.10
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






 

import vjo.lang.* ;

/**
 * Signals that an error occurred while attempting to connect a
 * socket to a remote address and port.  Typically, the connection
 * was refused remotely (e.g., no process is listening on the 
 * remote address/port).
 *
 * @since   JDK1.1
 */
public class ConnectException extends SocketException {
    /**
     * Constructs a new ConnectException with the specified detail 
     * message as to why the connect error occurred.
     * A detail message is a String that gives a specific 
     * description of this error.
     * @param msg the detail message
     */
    public ConnectException(String msg) {
	super(msg);
    }

    /**
     * Construct a new ConnectException with no detailed message.
     */
    public ConnectException() {}
}
