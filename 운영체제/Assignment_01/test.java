package 운영체제.Assignment_01;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class test {
  public static class Member {
    private String num;
    private String FirstName;
    private String LastName;

    public Member(String num, String FirstName, String LastName) {
      this.num = num;
      this.FirstName = FirstName;
      this.LastName = LastName;
    }
  }

  public static void main(String[] args) throws IOException {
    BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("운영체제/Assignment_01/test.out"));
    BufferedReader br = new BufferedReader(new FileReader("운영체제/Assignment_01/1.inp"));
    // BufferedOutputStream bs = new BufferedOutputStream(new
    // FileOutputStream("members[i].LastName"));
    // BufferedReader br = new BufferedReader(new FileReader("test.inp"));
    StringBuilder sb = new StringBuilder();
    int N = Integer.parseInt(br.readLine());
    Member[] members = new Member[N];
    HashSet<String> lastName = new HashSet<>();
    HashMap<String, Integer> map = new HashMap<>();
    for (int i = 0; i < N; i++) {
      String[] line = br.readLine().split(" ");
      members[i] = new Member(line[0], line[1], line[2]);
      lastName.add(line[2]);
      if (!map.containsKey(line[2])) {
        map.put(line[2], 1);
      } else {
        map.put(line[2], map.get(line[2]) + 1);
      }
    }
    Arrays.sort(members, (o1, o2) -> Integer.parseInt(o1.num) - Integer.parseInt(o2.num));
    ArrayList<String> lastNameList = new ArrayList<>(lastName);
    Collections.sort(lastNameList);
    int maxFirstName = 0;
    for (int i = 0; i < N; i++) {
      int length = members[i].FirstName.length();
      if (maxFirstName < length)
        maxFirstName = length;
    }
    String test = "%-" + maxFirstName + "s";
    for (int i = 0; i < N; i++) {
      // System.out.printf(members[i].num + " " + test + " " + members[i].LastName +
      // "\n", members[i].FirstName);
      sb.append(String.format(members[i].num + " " + test + " " + members[i].LastName + "\n", members[i].FirstName,
          members[i].FirstName + "\n"));
    }
    // System.out.println();
    sb.append('\n');
    for (int i = 0; i < lastNameList.size(); i++) {
      String key = lastNameList.get(i);
      int count = map.get(key);
      if (count > 1) {
        // System.out.println(key + " " + count);
        sb.append(key + " " + count + "\n");
      }
    }
    bs.write(sb.toString().getBytes());
    bs.close();
    br.close();
  }

}
