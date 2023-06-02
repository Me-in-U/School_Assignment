package 운영체제.Assignment_11;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public class page {
  protected static ArrayList<Integer> originData;
  public static int memSize;

  public static void main(String[] args) throws IOException {
    BufferedOutputStream bs = new BufferedOutputStream(new FileOutputStream("운영체제/Assignment_11/page.out"));
    BufferedReader br = new BufferedReader(new FileReader("운영체제/Assignment_11/1.inp"));
    memSize = Integer.parseInt(br.readLine());
    originData = new ArrayList<>();
    int frame = 0;
    while ((frame = Integer.parseInt(br.readLine())) != -1) {
      originData.add(frame);
    }
    StringBuilder sb = new StringBuilder();
    sb.append("FIFO: ").append(FIFO()).append('\n');
    sb.append("LRU: ").append(LRU()).append('\n');
    sb.append("OPT: ").append(OPT()).append('\n');
    bs.write(sb.toString().getBytes());
    bs.close();
  }

  private static int FIFO() {
    int pageFaultCount = 0;
    Set<Integer> memory = new LinkedHashSet<>();
    for (int i = 0; i < originData.size(); i++) {
      if (!memory.contains(originData.get(i))) {
        pageFaultCount++;
        if (memory.size() >= memSize) {
          Iterator<Integer> iterator = memory.iterator();
          memory.remove(iterator.next());
        }
        memory.add(originData.get(i));
      }
    }
    return pageFaultCount;
  }

  private static int LRU() {
    LinkedList<Integer> memory = new LinkedList<>();
    int pageFaultCount = 0;
    for (int i = 0; i < originData.size(); i++) {
      if (!memory.contains(originData.get(i))) {
        pageFaultCount++;
        if (memory.size() == memSize) {
          memory.removeFirst();
        }
        memory.addLast(originData.get(i));
      } else {
        memory.add(memory.remove(memory.indexOf(originData.get(i))));
      }
    }
    return pageFaultCount;
  }

  private static int OPT() {
    LinkedList<Integer> memory = new LinkedList<>();
    int pageFaultCount = 0;
    for (int pointer = 0; pointer < originData.size(); pointer++) {
      int pageToAdd = originData.get(pointer);
      if (!memory.contains(pageToAdd)) {
        if (memory.size() < memSize) {
          memory.add(pageToAdd);
        } else {
          int indexToRemove = -1;
          int farthestIdx = -1;
          for (int memIdx = 0; memIdx < memory.size(); memIdx++) {
            int farthestIdxForCurrentPage = -1;
            for (int findFarthestIndex = pointer; findFarthestIndex < originData.size(); findFarthestIndex++) {
              if (originData.get(findFarthestIndex) == memory.get(memIdx)) {
                farthestIdxForCurrentPage = findFarthestIndex;
                break;
              }
            }
            if (farthestIdxForCurrentPage == -1) {
              indexToRemove = memIdx;
              break;
            }
            if (farthestIdxForCurrentPage > farthestIdx) {
              farthestIdx = farthestIdxForCurrentPage;
              indexToRemove = memIdx;
            }
          }
          memory.set(indexToRemove, pageToAdd);
        }
        pageFaultCount++;
      }
    }
    return pageFaultCount;
  }
}
