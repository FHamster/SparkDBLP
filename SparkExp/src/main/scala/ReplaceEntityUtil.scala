import scala.util.matching.Regex

/**
 * dtd实体转换的工具类
 */
object ReplaceEntityUtil {
  //匹配 &reg; 模式的正则
  private val regex: Regex = new Regex("&[A-Za-z]*;")

  def parse(s: String): String = regex.replaceAllIn(s, it => it.toString() match {
    case "&reg;" => "&#174;"
    case "&micro;" => "&#181;"
    case "&times;" => "&#215;"
    case "&Agrave;" => "&#192;"
    case "&Aacute;" => "&#193;"
    case "&Acirc;" => "&#194;"
    case "&Atilde;" => "&#195;"
    case "&Auml;" => "&#196;"
    case "&Aring;" => "&#197;"
    case "&AElig;" => "&#198;"
    case "&Ccedil;" => "&#199;"
    case "&Egrave;" => "&#200;"
    case "&Eacute;" => "&#201;"
    case "&Ecirc;" => "&#202;"
    case "&Euml;" => "&#203;"
    case "&Igrave;" => "&#204;"
    case "&Iacute;" => "&#205;"
    case "&Icirc;" => "&#206;"
    case "&Iuml;" => "&#207;"
    case "&ETH;" => "&#208;"
    case "&Ntilde;" => "&#209;"
    case "&Ograve;" => "&#210;"
    case "&Oacute;" => "&#211;"
    case "&Ocirc;" => "&#212;"
    case "&Otilde;" => "&#213;"
    case "&Ouml;" => "&#214;"
    case "&Oslash;" => "&#216;"
    case "&Ugrave;" => "&#217;"
    case "&Uacute;" => "&#218;"
    case "&Ucirc;" => "&#219;"
    case "&Uuml;" => "&#220;"
    case "&Yacute;" => "&#221;"
    case "&THORN;" => "&#222;"
    case "&szlig;" => "&#223;"
    case "&agrave;" => "&#224;"
    case "&aacute;" => "&#225;"
    case "&acirc;" => "&#226;"
    case "&atilde;" => "&#227;"
    case "&auml;" => "&#228;"
    case "&aring;" => "&#229;"
    case "&aelig;" => "&#230;"
    case "&ccedil;" => "&#231;"
    case "&egrave;" => "&#232;"
    case "&eacute;" => "&#233;"
    case "&ecirc;" => "&#234;"
    case "&euml;" => "&#235;"
    case "&igrave;" => "&#236;"
    case "&iacute;" => "&#237;"
    case "&icirc;" => "&#238;"
    case "&iuml;" => "&#239;"
    case "&eth;" => "&#240;"
    case "&ntilde;" => "&#241;"
    case "&ograve;" => "&#242;"
    case "&oacute;" => "&#243;"
    case "&ocirc;" => "&#244;"
    case "&otilde;" => "&#245;"
    case "&ouml;" => "&#246;"

    case "&oslash;" => "&#248;"
    case "&ugrave;" => "&#249;"
    case "&uacute;" => "&#250;"
    case "&ucirc;" => "&#251;"
    case "&uuml;" => "&#252;"
    case "&yacute;" => "&#253;"
    case "&thorn;" => "&#254;"
    case "&yuml;" => "&#255;"

    case "&quot;" => "&#34;"
    case "&amp;" => "&#38;"
    case "&lt;" => "&#60;"
    case "&gt;" => "&#62;"
    case "&nbsp;" => "&#160;"
    case "&apos;" => "&#39;"
    //这里只是为了打印出不在匹配条件里面的情况
    case _ => {
      println(it)
      it.toString()
    }
  })


}
