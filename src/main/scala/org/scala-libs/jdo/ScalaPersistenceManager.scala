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

trait ScalaPersistenceManager {

  protected def pm : PersistenceManager

  val factory : ScalaPMFactory 

/*
  def findAll[A](queryName : String, params : Pair[String,Any]*) = 
    createAndParamify[A](queryName, params).findAll
  private def createAndParamify[A](queryName : String, 
                                   params : Seq[Pair[String,Any]]) : ScalaQuery[A] = {
    val q = createNamedQuery[A](queryName)
    params.foreach{ case (p1, p2) => q.setParameter(p1, p2)}
    q
  } 
  def createNamedQuery[A](queryName : String, params : Pair[String,Any]*) : ScalaQuery[A] = createAndParamify[A](queryName,params)
  def createNamedQuery[A](queryName: String) = 
    new ScalaQuery[A](pm.createNamedQuery(queryName)) 
*/
}
