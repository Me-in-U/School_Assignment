package 문제해결기법.Assignment_29;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class tree20 {
    private static Map<String, String> parentMap = new HashMap<>();
    private static Set<String> names = new HashSet<>();

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_29\\tree.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_29\\1.inp"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(" ");
            if (parts[0].equals("no.child") && parts[1].equals("no.parent")) {
                break;
            }
            String child = parts[0];
            String parent = parts[1];
            parentMap.put(child, parent);
            names.add(child);
            names.add(parent);
        }
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(" ");
            String p = parts[0];
            String q = parts[1];
            String relationship = findRelationship(p, q);
            sb.append(relationship).append('\n');
        }
        bw.write(sb.toString());
        bw.close();
        br.close();
    }

    private static String findRelationship(String p, String q) {
        if (!(parentMap.containsKey(p) && parentMap.containsKey(q))) {
            return "no relation";
        }
        if (parentMap.get(p).equals(parentMap.get(q))) {
            return "sibling";
        }
        if (parentMap.get(p).equals(q)) {
            return "child";
        }
        if (parentMap.get(q).equals(p)) {
            return "parent";
        }

        int pToQDescendant = getDescendantLevel(p, q);
        int qToPDescendant = getDescendantLevel(q, p);

        if (pToQDescendant > 0) {
            return descendantString(pToQDescendant);
        }
        if (qToPDescendant > 0) {
            return ancestorString(qToPDescendant);
        }

        int[] cousinInfo = getCousinInfo(p, q);
        if (cousinInfo[0] != -1) {
            int k = cousinInfo[0] - 1;
            int j = cousinInfo[1];
            return (j == 0) ? (k + " cousin") : (k + " cousin removed " + j);
        }
        return "no relation";
    }

    private static int getDescendantLevel(String child, String ancestor) {
        int level = 0;
        while (parentMap.containsKey(child)) {
            if (parentMap.get(child).equals(ancestor)) {
                return level + 1;
            }
            child = parentMap.get(child);
            level++;
        }
        return -1;
    }

    private static String descendantString(int level) {
        if (level == 1)
            return "child";
        if (level == 2)
            return "grand child";
        return repeat("great ", level - 2) + "grand child";
    }

    private static String ancestorString(int level) {
        if (level == 1)
            return "parent";
        if (level == 2)
            return "grand parent";
        return repeat("great ", level - 2) + "grand parent";
    }

    private static int[] getCousinInfo(String p, String q) {
        Map<String, Integer> pAncestors = new HashMap<>();
        Map<String, Integer> qAncestors = new HashMap<>();
        getAncestors(p, pAncestors);
        getAncestors(q, qAncestors);

        String lca = null;
        int pDist = -1;
        int qDist = -1;
        for (Map.Entry<String, Integer> entry : pAncestors.entrySet()) {
            String ancestor = entry.getKey();
            int pDistance = entry.getValue();
            if (qAncestors.containsKey(ancestor)) {
                int qDistance = qAncestors.get(ancestor);
                if (lca == null || (pDistance + qDistance) < (pDist + qDist)) {
                    lca = ancestor;
                    pDist = pDistance;
                    qDist = qDistance;
                }
            }
        }

        if (lca == null)
            return new int[] { -1, -1 };

        int k = Math.min(pDist, qDist);
        int j = Math.abs(pDist - qDist);
        return new int[] { k, j };
    }

    private static void getAncestors(String node, Map<String, Integer> ancestors) {
        int level = 0;
        while (parentMap.containsKey(node)) {
            ancestors.put(node, level);
            node = parentMap.get(node);
            level++;
        }
        ancestors.put(node, level);
    }

    private static String repeat(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}