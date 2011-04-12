/*******************************************************************************
 * Copyright (c) 2005-2011 eBay Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.ebayopensource.dsf.common.statistics;


public class BizMoHistogram extends BaseHistogram {

   public BizMoHistogram() {
      super();
   }

   public String getName() {
      return V4StatisticsUtil.BIZMO_HISTOGRAM_NAME;
   }

   public String getHistogramName() {
      return V4StatisticsUtil.BIZMO_HISTOGRAM_NAME;
   }

   public String getSubObjName() {
      return V4StatisticsUtil.BIZMO;
   }

   public static BizMoHistogram getHistogram() {
      return (BizMoHistogram) V4StatisticsContainer.getInstance().get(
            V4StatisticsUtil.BIZMO_HISTOGRAM_NAME);
   }
}
