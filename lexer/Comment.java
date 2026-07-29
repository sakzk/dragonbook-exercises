
package lexer;
public class Comment extends Token {
  public final String lexeme;
  public Comment(int t, String s) {
    super(t); lexeme = new String(s);
  }

  @Override
  public String toString() {
      // タグが Tag.ID(通常は264など) なのか、予約語(true/false)なのかが分かるように出力
      return "<COMMENT, tag: " + tag + ", lexeme: \"" + lexeme + "\">";
  }
}
