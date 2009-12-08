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
package com.jcraft.lift.snippet

import scala.xml.{NodeSeq,Text}

import net.liftweb.http.{RequestVar,S,SHtml}
import net.liftweb.util.{Helpers,Log}
import S._
import Helpers._

import _root_.com.jcraft.lift.model._
import _root_.com.jcraft.lift.model.Model._
import _root_.org.scala_libs.jdo._
import _root_.org.scala_libs.jdo.criterion._

class AuthorOps {

  def list (xhtml : NodeSeq) : NodeSeq = {
    val authors = Model.withPM{ from(_, classOf[Author]).resultList }

    def findBooksByAuthor(a:Author) = {
      Model.withPM{ 
        from(_, classOf[Book])
            .where(eqC("author", a))
            .resultList 
      }
    }

    authors.flatMap(author =>
      bind("author", xhtml,
           "name" -> Text(author.name),
           "count" -> SHtml.link("/books/search", 
                                 () => BookOps.resultVar(findBooksByAuthor(author)),
                                 Text(author.books.size.toString)),

           "edit" -> SHtml.link("add.html", 
                                () => authorVar(author),
                                Text(?("Edit"))),
           "delete" -> SHtml.link("list.html", 
                                () => Model.withPM{ _.deletePersistent(author)},
                                Text(?("Delete")))))
  }

  object authorVar extends RequestVar(new Author())
  lazy val author = authorVar.is

  def add (xhtml : NodeSeq) : NodeSeq = {
    def doAdd () = {
      if (author.name.length == 0) {
        error("emptyAuthor", "The author's name cannot be blank")
      } 
      else {
        Model.withPM{ _.makePersistent(author) }
        redirectTo("list.html")
      }
    }

    bind("author", xhtml,
	 "name" -> SHtml.text(author.name,  author.name=_ ),
	 "submit" -> SHtml.submit(?("Save"), doAdd))
  }
}
