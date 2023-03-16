package 운영체제.Assignment_02;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class command {
  public static void main(String[] args) throws IOException {
    BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("command.out"));
    BufferedReader br = new BufferedReader(new FileReader("command.inp"));
    Map<String, String> map = new HashMap<>();
    map.put("ls", "dir");
    map.put("mkdir", "md");
    map.put("rmdir", "rd");
    map.put("rm", "del");
    map.put("cp", "copy");
    map.put("mv", "rename");
    map.put("clear", "cls");
    map.put("pwd", "cd");
    map.put("cat", "type");
    map.put("man", "help");
    map.put("date", "time");
    map.put("find", "find");
    map.put("grep", "findstr");
    map.put("more", "more");
    map.put("diff", "comp");
    map.put("ed", "edlin");
    map.put("sort", "sort");
    map.put("lsattr", "attrib");
    map.put("pushd", "pushd");
    map.put("popd", "popd");
    map.put("ps", "taskmgr");
    map.put("kill", "tskill");
    map.put("halt", "shutdown");
    map.put("ifconfig", "ipconfig");
    map.put("fsck", "chkdsk");
    map.put("free", "mem");
    map.put("debugfs", "scandisk");
    map.put("lpr", "print");
    StringBuilder sb = new StringBuilder();
    int N = Integer.parseInt(br.readLine());
    for (int i = 0; i < N; i++) {
      String input = br.readLine();
      if (map.containsKey(input)) {
        sb.append(input + " -> " + map.get(input)).append('\n');
      } else {
        for (Entry<String, String> entry : map.entrySet()) {
          if (entry.getValue().equals(input)) {
            sb.append(input + " -> " + entry.getKey()).append('\n');
            break;
          }
        }
      }
    }
    bs.write(sb.toString().getBytes());
    bs.close();
    br.close();
  }
}