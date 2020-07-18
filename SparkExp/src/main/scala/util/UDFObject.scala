package util

object UDFObject {
  def dblpType(_publType: String, type_xml: String, prefix1: String): String = {
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
}