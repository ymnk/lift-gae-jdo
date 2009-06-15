/*
 * Copyright 2009 ymnk, JCraft,Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */

package org.scala_libs.jdo

import javax.jdo.{JDOHelper, PersistenceManager}

class LocalPMFactory(val unitName : String, val userTx : Boolean) 
  extends ScalaPMFactory {

  def this(unitName : String) = this(unitName, false)
 
  private val pmf = JDOHelper.getPersistenceManagerFactory(unitName)
 
  def openPM () = {
    val pm = pmf.getPersistenceManager
 
//  if (!userTx) { pm.getTransaction.begin() }
 
    pm
  }
 
  def closePM (pm : PersistenceManager) = {
/*
    if (!userTx) {
      if (pm.getTransaction.getRollbackOnly) {
        pm.getTransaction.rollback()
      } else {
        pm.getTransaction.commit()
      }
    }
*/
    pm.close()
  }
}
