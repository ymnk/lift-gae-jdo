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

class LocalPMFactory(val unitName : String) 
  extends ScalaPMFactory {

  private val pmf = JDOHelper.getPersistenceManagerFactory(unitName)
 
  def openPM () = {
    pmf.getPersistenceManager
  }
 
  def closePM (pm : PersistenceManager) = {
    pm.close()
  }

  def withPM[A](f: PersistenceManager => A):A={
    val _pm = openPM
    try{ f(_pm) }
    finally{ closePM(_pm) }   
  }

  def inTX[A](f: PersistenceManager => A):A={
    val _pm = openPM
    val tx = _pm.currentTransaction
    try{ 
      tx.begin
      f(_pm) match {
        case a => tx.commit; a
      } 
    }
    finally{
      if(tx.isActive){
        tx.rollback
      }
      closePM(_pm) 
    }   
  }

}
