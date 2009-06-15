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

import _root_.java.text.{ParseException,SimpleDateFormat}
import _root_.scala.xml.{NodeSeq,Text}
import _root_.net.liftweb.http.{RequestVar,S,SHtml}
import _root_.net.liftweb.util.{Box,Empty,Full,Helpers,Log}
import S._
import Helpers._

import _root_.com.jcraft.lift.model._
import _root_.com.jcraft.lift.model.Model._

import _root_.javax.persistence.{EntityExistsException,PersistenceException}

import _root_.scala.collection.jcl.Conversions.convertList

import _root_.com.google.appengine.api.datastore.Key
import _root_.com.google.appengine.api.datastore.KeyFactory._

import org.scala_libs.jdo._

object BookOps {
  object resultVar extends RequestVar[List[Book]](Nil)
}

class BookOps {
  val formatter = new java.text.SimpleDateFormat("yyyyMMdd")

  def list (xhtml : NodeSeq) : NodeSeq = {
    val books = {
      Model{ case pm =>
        val query = pm.newQuery(classOf[Book])
        try{ new ScalaQuery[Book](query).getResultList }
        finally{query.closeAll}
      }
    }

    books.flatMap(book =>
      bind("book", xhtml,
	   "title" -> Text(book.title),
	   "published" -> Text(formatter.format(book.published)),
	   "genre" -> Text(if(book.genre != null) book.genre.toString else ""),
	   "author" -> Text(book.author.name),
	   "edit" -> SHtml.link("add.html", 
                                () => bookVar(Some(book.id)), 
                                Text(?("Edit")))))
  }

  object bookVar extends RequestVar[Option[Key]](None)
  lazy val book = bookVar.is match {
    case Some(key) => {
      Model{ case pm =>
        val foo=pm.getObjectById(classOf[Book], key).asInstanceOf[Book]
          pm.detachCopy(foo.author)
          val books = foo.author.books
          books.get(books.indexOf(foo))
      }
    }
    case _ => new Book()
  }

  def is_valid_Book_? (toCheck : Book) : Boolean ={
    List((if (toCheck.title.length == 0) { 
            S.error("You must provide a title"); false 
           } else true),
	 (if (toCheck.published == null) { 
            S.error("You must provide a publish date"); false 
          } else true),
	 (if (toCheck.genre == null) { 
            S.error("You must select a genre"); false } else true),
	 (if (toCheck.author == null) {
	    S.error("You must select an author"); false
	  } else true)
         ).forall(_ == true)
  }

  def setDate (input : String, toSet : Book) {
    try { toSet.published=formatter.parse(input) } 
    catch { case pe : ParseException => S.error("Error parsing the date") }
  }

  def findAuthor(a:Author):Author = findAuthorById(a.id)
  def findAuthorById(id:String):Author = findAuthorById(stringToKey(id))
  def findAuthorById(id:Key):Author = {
    Model{ case pm =>
      val a=pm.getObjectById(classOf[Author], id).asInstanceOf[Author]
      pm.detachCopyAll(a.books)
      a
    }
  }

  def add (xhtml : NodeSeq) : NodeSeq = {

    def doAdd () = 
      if (is_valid_Book_?(book)) {
	try{
          Model{ case pm =>
            book.id match {
              case null => book.author.books.add(book)
              case _ => 
            }
            pm.makePersistent(book.author)
          }
	  redirectTo("list")
	} catch {
	  case ee : EntityExistsException => 
            error("That book already exists.")
	  case pe : PersistenceException => 
            error("Error adding book"); Log.error("Book add failed", pe)
	}
      }

    lazy val current = book

    val authors = {
      Model{ case pm =>
        val query = pm.newQuery(classOf[Author])
        try{ new ScalaQuery[Author](query).getResultList }
        finally{query.closeAll}
      }
    }

    val choices = authors.map(author => 
      (keyToString(author.id) -> author.name)).toList
    val default = 
       if (book.author != null) { Full(keyToString(book.author.id)) } 
       else { Empty }

    def find(n:String):Author = {
       val l = for(a<-authors if a.id.toString==n) yield  a
      l(0)
    }

    import SHtml.{hidden, text, select, submit}
    bind("book", xhtml,
	 "id" -> hidden(() => bookVar(Some(current.id))),
	 "title" -> text(book.title, book.title=_),
	 "published" -> text(formatter.format(book.published), 
                                   setDate(_, book)) % ("id" -> "published"),
	 "genre" -> select(Genre.getNameDescriptionList, 
                          (Box.legacyNullTest(book.genre).map(_.toString) or Full("")), 
                          choice => book.genre = Genre.valueOf(choice).getOrElse("").toString),
	 "author" -> select(choices, 
                            default, 
                            (id) => if(book.author==null)
                                      book.author=findAuthorById(id)),
	 "save" -> submit(?("Save"), doAdd))
  }

  def searchResults (xhtml : NodeSeq) : NodeSeq = 
    BookOps.resultVar.is.flatMap(result =>
      bind("result", xhtml, 
           "title" -> Text(result.title), 
           "author" -> Text(result.author.name)))

  def search (xhtml : NodeSeq) : NodeSeq = {
    var title = ""

    def doSearch () = {
      val l = Model{ case pm =>
        val query = pm.newQuery(classOf[Book])
        try{
	  query.setFilter("title == searchedTitle")
          query.declareParameters("java.lang.String searchedTitle")
          query.execute(title).asInstanceOf[java.util.List[Book]]
        }
        finally{
          query.closeAll
        }
      }
      BookOps.resultVar(convertList[Book](l).toList)
    }

    bind("search", xhtml,
	 "title" -> SHtml.text(title, title = _),
	 "run" -> SHtml.submit(?("Search"), doSearch _))
  }

}