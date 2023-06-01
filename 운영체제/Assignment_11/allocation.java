package 운영체제.Assignment_11;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class allocation {
  public static ArrayList<Integer> originData;
  public static int memSize;

  public static void main(String[] args) throws IOException {
    BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("운영체제/Assignment_11/allocation.out"));
    BufferedReader br = new BufferedReader(new FileReader("운영체제/Assignment_11/1.inp"));
    memSize = Integer.parseInt(br.readLine());
    originData = new ArrayList<>();
    int frame = 0;
    while ((frame = Integer.parseInt(br.readLine())) != -1) {
      originData.add(frame);
    }
    StringBuilder sb = new StringBuilder();
    sb.append("FIFO: ").append(FIFO()).append('\n');
    sb.append("OPT: ").append(OPT()).append('\n');
    bs.write(sb.toString().getBytes());
    bs.close();
  }

  private static int FIFO() {
    int pageFaults = 0;
    Set<Integer> memory = new LinkedHashSet<>();
    for (int i = 0; i < originData.size(); i++) {
      if (!memory.contains(originData.get(i))) {
        pageFaults++;
        if (memory.size() >= memSize) {
          Iterator<Integer> iterator = memory.iterator();
          memory.remove(iterator.next());
        }
        memory.add(originData.get(i));
      }
    }
    return pageFaults;
  }

  private static int LRU() {
    return 0;
  }

  private static int OPT() {
    int pageFaults = 0;
    Set<Integer> memory = new HashSet<>();
    Map<Integer, Integer> pageIndexes = new HashMap<>();
    for (int i = 0; i < originData.size(); i++) {
      int page = originData.get(i);
      if (!memory.contains(page)) {
        pageFaults++;
        if (memory.size() >= memSize) {
          int farthestIndex = -1; // 가장 먼 참조 인덱스
          int pageToReplace = -1; // 교체할 페이지
          for (int j : memory) { // 현재 메모리에 있는 페이지들을 순회
            if (!pageIndexes.containsKey(j)) { // 이후에 참조되지 않는 페이지인 경우
              pageToReplace = j; // 교체할 페이지로 선택
              break;
            }
            int index = pageIndexes.get(j); // 페이지의 마지막 참조 인덱스
            if (index > farthestIndex) { // 가장 먼 참조 인덱스보다 큰 경우
              farthestIndex = index; // 가장 먼 참조 인덱스를 업데이트
              pageToReplace = j; // 교체할 페이지로 선택
            }
          }
          memory.remove(pageToReplace); // 페이지 교체
        }
        memory.add(page);
      }
      pageIndexes.put(page, i); // 페이지의 마지막 참조 인덱스를 업데이트
    }
    return pageFaults; // 페이지 부재 수 반환
  }
}