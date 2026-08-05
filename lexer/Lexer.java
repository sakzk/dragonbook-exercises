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
  // EOF のときは､scan() は token.tag=65535 を返す｡ (System.in.read() で､EOFは -1になり､そいつを､charでキャストしているため｡
  public Token scan() throws IOException {
    // 空白類文字の読み飛ばし
    for (;; peek = (char)System.in.read()){
      if (peek == ' ' || peek == '\t')
        continue;
      else if (peek == '\n')
        line = line + 1;
      else break;
    }
    // TODO: コメントのサポート
    /* 無限ループになります
    ========================================
    入力文字列:
    // this line is comment
    ----------------------------------------
    ^C%
    */
    if (peek == '/'){
      StringBuffer b = new StringBuffer();
      b.append(peek);
      peek = (char)System.in.read();
      // 行コメント
      if (peek == '/'){
        while (peek != '\n' && peek != (char)65535){ // TODO: もっと良い書き方がある気がする
          b.append(peek);
          peek = (char)System.in.read();
        }
      }
      if (peek == '\n') {
        line = line + 1;
      }

      // ブロックコメント
      String s = b.toString();
      Comment c = new Comment(Tag.ID, s);
      return c;
    }

    // 算術演算子
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
    if (Character.isLetter(peek)){ // Character.isLetter は何が含まれる? 特殊記号も含まれる?
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
    // 文字列の最後だと､peek == 65535 になる
    Token t = new Token(peek);
    peek = ' ';
    return t;
  }

  // 🚀 テストを実行するための共通メソッド（関数化）
  public static void runTest(String testInput) {
    // System.in (標準入力) を一時的にテスト用文字列に置き換える
    InputStream originalIn = System.in;
    System.setIn(new ByteArrayInputStream(testInput.getBytes()));
    int token_count = 0;

    try {
      System.out.println("\n========================================");
      System.out.println("入力文字列:\n" + testInput);
      System.out.println("----------------------------------------");

      Lexer lexer = new Lexer();

      // 指定された回数だけトークンをスキャンする
      // for (int i = 0; i < expectedTokenCount; i++) {
      //   Token token = lexer.scan();

      //   // トークンの情報を表示
      //   System.out.println("Token " + (i + 1) + ": " + token.toString() + " (Tag: " + token.tag + ")");
      // }
      while (true){
        Token token = lexer.scan();
        // token.Tag.equals(""); バグ
        if (token.tag == 65535){
          break;
        }
        System.out.println("Token " + (token_count + 1) + ": " + token.toString() + " (Tag: " + token.tag + ")");
        token_count++;
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

    // 実験
    runTest("");
      // -> Token 1: <Tag: 65535> (Tag: 65535)

    // ケース1: 元のハッピーパス (トークン5個分)
    // 期待されるトークン: true, 46, false, apple, ;
    runTest("true 46\nfalse apple;");

    // ケース2: 数値と演算子（半角スペース区切り）(トークン5個分)
    // 期待されるトークン: 10, +, 20, =, 30
    runTest("10 + 20 = 30");

    // ケース3: タブや改行が入り混じったパターン (トークン4個分)
    // 期待されるトークン: true, count, 999, false
    runTest("\ttrue\t\n  count  \n\n999 false");

    // ケース4: 予約語の直後に文字が続くパターン（識別子として処理されるかのテスト） (トークン1個分)
    // 期待されるトークン: trueabc (単なるIDであり、予約語のtrueとは別物になるはず)
    runTest("trueabc");

    // ケース5: 行コメント
    runTest("// this line is comment\n"); // 改行ありバージョン [調査] 改行をリテラルで書くための記法
    runTest("//");
    runTest("10 + 20 = 30 //");
    runTest("10 + 20 = 30 // meaning less");

    System.out.println("\n--- すべてのテストケースの呼び出しが完了しました ---");
  }
}
