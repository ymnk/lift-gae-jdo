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
package com.jcraft.lift.model

import scala.collection.mutable.Map

object Genre extends Enumeration{

  private var nameDescriptionMap = Map[String, String]()

  private def Value(name: String, desc: String) : Value = {
    nameDescriptionMap += (name -> desc)
    new Val(name)
  }

  val Mystery = Value("Mystery", "Mystery")
  val SciFi = Value("SciFi", "SciFi")
  val Classic = Value("Classic", "Classic")
  val Childrens = Value("Childrens", "Childrens")
  val Horror = Value("Horror", "Horror")
  val Poetry = Value("Poetry", "Poetry")
  val unknown = Value("Unknown", "Unknown genre")

  def getDescriptionOrName(ev: this.Value) = {
    try { nameDescriptionMap(""+ev) } 
    catch {  case e: NoSuchElementException => ev.toString }
  }

  def getNameDescriptionList =  
    elements.toList.map(v => (v.toString, getDescriptionOrName(v))).toList
}
