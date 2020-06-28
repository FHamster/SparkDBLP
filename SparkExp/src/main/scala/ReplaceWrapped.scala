import scala.util.matching.Regex

object ReplaceWrapped {
  val regex: Regex = new Regex("\\[WrappedArray\\(|\\),|\\]|\\[null");

  def wpParse(s: String): String = regex.replaceAllIn(s, it => it.toString() match{
    case "[WrappedArray(" => ""
    case ")," => ")"
    case "[null" => "noauthor)"
    case "]" => "|"
    case _ => {
      println(it)
      it.toString()
    }
  })
}
