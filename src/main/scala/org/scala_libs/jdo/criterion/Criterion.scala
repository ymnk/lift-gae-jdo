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
package org.scala_libs.jdo.criterion

trait Criterion{
  val property:String
}

abstract class FilterCriterion(val pattern:String) extends Criterion {
  val parameter:AnyRef
  def queryString(parameterName:String):String =
    pattern.format(property, parameterName)
}

case class containsC(override val property:String, override val parameter:AnyRef) 
             extends FilterCriterion("%s.contains(%s)")

case class eqC(override val property:String, override val parameter:AnyRef)
             extends FilterCriterion("%s == %s")

case class geC(override val property:String, override val parameter:AnyRef)
             extends FilterCriterion("%s >= %s")

case class gtC(override val property:String, override val parameter:AnyRef)
             extends FilterCriterion("%s > %s")

case class leC(override val property:String, override val parameter:AnyRef)
             extends FilterCriterion("%s <= %s")

case class ltC(override val property:String, override val parameter:AnyRef)
             extends FilterCriterion("%s < %s")

abstract class OrderCriterion(val pattern:String) extends Criterion{
  def queryString():String = property + pattern
}

case class ascC(override val property:String) extends OrderCriterion(" asc")
case class descC(override val property:String) extends OrderCriterion(" desc")
