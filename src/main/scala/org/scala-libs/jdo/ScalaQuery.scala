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

import javax.jdo._
import _root_.scala.collection.jcl.Conversions.convertList
import _root_.java.util.{Date,Calendar}
 
class ScalaQuery[A](val query: Query) {

  def findAll = getResultList()

  def getResultList() = 
    convertList[A](query.execute.asInstanceOf[java.util.List[A]])
/* 
  def findOne = Utils.findToOption[A](query.getSingleResult.asInstanceOf[A])
  def setParams(params : Pair[String,Any]*) = {params.foreach(param => query.setParameter(param._1, param._2)); this}
  def getResultList() = Conversions.convertList[A](query.getResultList.asInstanceOf[java.util.List[A]])
  def getSingleResult() = query.getSingleResult.asInstanceOf[A]
  def executeUpdate() = query.executeUpdate()
  def setMaxResults(maxResult: Int) = {query.setMaxResults(maxResult);this}
  def setFirstResult(startPosition: Int) = {query.setFirstResult(startPosition); this}
  def setHint(hintName: String, value: Any) = {query.setHint(hintName, value); this}
  def setParameter(name: String, value: Any) = {query.setParameter(name, value); this}
  def setParameter(position: Int, value: Any) = {query.setParameter(position, value); this}
  def setParameter(name: String, value: Date, temporalType: TemporalType) = {query.setParameter(name, value, temporalType); this}
  def setParameter(position: Int, value: Date, temporalType: TemporalType) = {query.setParameter(position, value, temporalType); this}
  def setParameter(name: String, value: Calendar, temporalType: TemporalType) = {query.setParameter(name, value, temporalType); this}
  def setParameter(position: Int, value: Calendar, temporalType: TemporalType) = {query.setParameter(position, value, temporalType); this}
  def setFlushMode(flushMode: FlushModeType) = {query.setFlushMode(flushMode); this}
*/ 
}
 
 
 
