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
import javax.jdo.annotations._
import com.google.appengine.api.datastore.Key

/**
  An author is someone who writes books.
*/
@PersistenceCapable{val identityType = IdentityType.APPLICATION,
                    val detachable="true"}
class Author {

  @PrimaryKey
  @Persistent{val valueStrategy = IdGeneratorStrategy.IDENTITY}
  var id : Key = _

  @Persistent
  var name : String = ""

  @Persistent{val mappedBy = "author",
              val defaultFetchGroup="true"}
  var books : java.util.List[Book] = new java.util.LinkedList[Book]
}
