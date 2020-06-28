import ReplaceEntityUtil.regex

import scala.util.matching.Regex

object ReplaceTagUtil {
  private val atorRegex: Regex = new Regex("\\<(sub|i|sup|/i|/sub|/sup|tt|/tt)\\>");

  private val rtoaRegex: Regex = new Regex("\\((sub|i|sup|/i|/sub|/sup|tt|/tt)\\)");

  //我就只改了<i>->(i)和</i>->(/i)
  /*
   * (i)
   * (/i)
   * <title>The Many-Valued <i> sd </i> Theorem Prover<sub>3</sub><sub>3</sub>T<sup>A</sup>P. (i) i (/i)</title>
   */
  //根据输出的来看，是<i>走了通配的情况，也就是 _ 分支，所以才打出来了
  //说实话我不太明白\是做什么的,这里应该不用转义
  def atorParse(s: String): String = atorRegex.replaceAllIn(s, it => it.toString() match{
    case "<i>" => "(i)"
    case "<tt>" => "(tt)"
    case "<sub>" => "(sub)"
    case "<sup>" => "(sup)"
    case "</i>" => "(/i)"
    case "</tt>" => "(/tt)"
    case "</sub>" => "(/sub)"
    case "</sup>" => "(/sup)"
    case _ => {
      println(it)
      it.toString()
    }
  })

  //
  def rtoaAarse(s: String): String = rtoaRegex.replaceAllIn(s, it => it.toString() match{
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