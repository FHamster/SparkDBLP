package util

import util.ReplaceTagUtil.rtoaRegex

import scala.util.matching.Regex

object UDFObject {
  val dblpType = (_publType: String, type_xml: String, prefix1: String) => {
    (_publType, type_xml, prefix1) match {
      // publitype 是informal的基本都是 informal publication类型
      case ("informal", _, _) => "informal"
      case ("withdrawn", _, _) => "withdrawn"
      case (_, _, "reference/") => "reference"
      case (_, _, "series/") => "series"
      // prefix为conf是conference and workshop
      case (_, "article", "conf/") => "conference and workshop"
      case (_, "article", "journals/") => "journals article"
      case (_, "article", _) => "other article"
      case (_, "book", _) => "book and thesis"
      case (_, "phdthesis", _) => "book and thesis"
      case (_, "mastersthesis", _) => "book and thesis"
      case (_, "incollection", _) => "incollection"
      case (_, "inproceedings", _) => "inproceedings"
      case (_, "proceedings", _) => "proceedings"
      case (_, _, _) => "default"
      // editor字段不为null基本是series
      //      case ("series/", _) =>    "series/"
      // 同时拥有title和book title不为null，且prefix为book是parts in book
      //      case ("series/", _) =>    "series/"
      // prefix1 == books || php || persons基本是book
      //      case ("journals/", _) =>    "journals/"
    }
  }

  def dblpType2(_publType: String, type_xml: String, prefix1: String): String = {
    (_publType, type_xml, prefix1) match {
      // publitype 是informal的基本都是 informal publication类型
      case ("informal", _, _) => "informal"
      case ("withdrawn", _, _) => "withdrawn"
      case (_, _, "reference/") => "reference"
      case (_, _, "series/") => "series"
      // prefix为conf是conference and workshop
      case (_, "article", "conf/") => "conference and workshop"
      case (_, "article", "journals/") => "journals article"
      case (_, "article", _) => "other article"
      case (_, "book", _) => "book and thesis"
      case (_, "phdthesis", _) => "book and thesis"
      case (_, "mastersthesis", _) => "book and thesis"
      case (_, "incollection", _) => "incollection"
      case (_, "inproceedings", _) => "inproceedings"
      case (_, "proceedings", _) => "proceedings"
      case (_, _, _) => "default"
      // editor字段不为null基本是series
      //      case ("series/", _) =>    "series/"
      // 同时拥有title和book title不为null，且prefix为book是parts in book
      //      case ("series/", _) =>    "series/"
      // prefix1 == books || php || persons基本是book
      //      case ("journals/", _) =>    "journals/"
    }
  }

  def writeNotNull(in1: String, in2: String, defaultValue: String): String = {
    if (in1 != null) {
      return in1
    }
    if (in2 != null) {
      return in2
    }
    defaultValue
  }

  private val rtoaRegex: Regex = new Regex("\\((sub|i|sup|/i|/sub|/sup|tt|/tt)\\)");

  def rtoaAarse: String => String = (s: String) =>
    rtoaRegex.replaceAllIn(s, it => it.toString() match {
    case "(i)" => "<i>"
    case "(tt)" => "<tt>"
    case "(sub)" => "<sub>"
    case "(sup)" => "<sup>"
    case "(/i)" => "</i>"
    case "(/tt)" => "</tt>"
    case "(/sub)" => "</sub>"
    case "(/sup)" => "</sup>"
    case _ =>"default"
      //      println(it)
//      it.toString()
    })

  def rtoaAarse2(s: String): String = rtoaRegex.replaceAllIn(s, it => it.toString() match {
    case "(i)" => "<i>"
    case "(tt)" => "<tt>"
    case "(sub)" => "<sub>"
    case "(sup)" => "<sup>"
    case "(/i)" => "</i>"
    case "(/tt)" => "</tt>"
    case "(/sub)" => "</sub>"
    case "(/sup)" => "</sup>"
    case _ => {
      println(it)
      it.toString()
    }
  })
}
