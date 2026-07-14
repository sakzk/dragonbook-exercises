package lexer;
import java.io.*;
import java.util.*;

public class Lexer{
  // クラスが持つ値
  public int line = 1;
  private char peek = ' ';
  Hashtable words = new Hashtable();
  void reserve(Word t) {words.put(t.lexeme, t);}; // なにこれ?

  // コンストラクタ
  public Lexer() {
    reserve( new Word(Tag.TRUE, "true"));
    reserve( new Word(Tag.FALSE, "false"));
  }

  // Scan()
  public Token scan() throws IOException {
    // 空白類文字の読み飛ばし
    for (;; peek = (char)System.in.read()){
      if (peek == ' ' || peek == '\t')
        continue;
      else if (peek == '\n')
        line = line + 1;
      else break;
    }
    // 数値
    if (Character.isDigit(peek)){
      int v = 0;
      do {
        v = v * 10 + Character.digit(peek, 10);
        peek = (char)System.in.read();
      } while (Character.isDigit(peek));
      return new Num(v);
    }
    // id
    if (Character.isLetter(peek)){
      StringBuffer b = new StringBuffer();
      do {
        b.append(peek);
        peek = (char)System.in.read();
      } while (Character.isLetterOrDigit(peek));
      // idの辞書への追加
      String s = b.toString();
      Word w = (Word)words.get(s);
      if (w != null) return w;
      w = new Word(Tag.ID, s);
      words.put(s, w);
      return w;

    }
    // 後片付け
    Token t = new Token(peek);
    peek = ' ';
    return t;
  }

  // 🚀 テストを実行するための共通メソッド（関数化）
  public static void runTest(String testInput, int expectedTokenCount) {
    // System.in (標準入力) を一時的にテスト用文字列に置き換える
    InputStream originalIn = System.in;
    System.setIn(new ByteArrayInputStream(testInput.getBytes()));

    try {
      System.out.println("\n========================================");
      System.out.println("入力文字列:\n" + testInput);
      System.out.println("----------------------------------------");

      Lexer lexer = new Lexer();

      // 指定された回数だけトークンをスキャンする
      for (int i = 0; i < expectedTokenCount; i++) {
        Token token = lexer.scan();

        // トークンの情報を表示
        System.out.println("Token " + (i + 1) + ": " + token.toString() + " (Tag: " + token.tag + ")");
      }

      System.out.println("----------------------------------------");
      System.out.println("最終行数: " + lexer.line);
      System.out.println("テスト終了");

    } catch (IOException e) {
      System.err.println("エラーが発生しました: " + e.getMessage());
    } finally {
      // ストリームを元に戻す（次のテストに影響を与えないため）
      System.setIn(originalIn);
    }
  }

  // 🚀 mainメソッドから色々なパターンを呼び出す
  public static void main(String[] args) {
    System.out.println("--- 字句解析（Lexer）複数ケーステスト開始 ---");

    // ケース1: 元のハッピーパス (トークン5個分)
    // 期待されるトークン: true, 46, false, apple, ;
    runTest("true 46\nfalse apple;", 5);

    // ケース2: 数値と演算子（半角スペース区切り）(トークン5個分)
    // 期待されるトークン: 10, +, 20, =, 30
    runTest("10 + 20 = 30", 5);

    // ケース3: タブや改行が入り混じったパターン (トークン4個分)
    // 期待されるトークン: true, count, 999, false
    runTest("\ttrue\t\n  count  \n\n999 false", 4);

    // ケース4: 予約語の直後に文字が続くパターン（識別子として処理されるかのテスト） (トークン1個分)
    // 期待されるトークン: trueabc (単なるIDであり、予約語のtrueとは別物になるはず)
    runTest("trueabc", 1);

    System.out.println("\n--- すべてのテストケースの呼び出しが完了しました ---");
  }
}
