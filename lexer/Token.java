package lexer;
public class Token {
  public final int tag;
  public Token(int t) {tag = t;}

  @Override
    public String toString() {
        // ASCII文字の範囲（可視文字）であれば、文字として出力する
        if (tag > 31 && tag < 127) {
            return "<'" + (char) tag + "'>";
        }
        // それ以外（NumやWordなど、Tagクラスで定義された256以上の値など）
        return "<Tag: " + tag + ">";
    }
}
