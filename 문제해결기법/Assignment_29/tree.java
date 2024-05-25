package 문제해결기법.Assignment_29;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class tree {
    private static Map<String, String> parentMap = new HashMap<>();
    private static Map<String, List<String>> childrenMap = new HashMap<>();
    private static Set<String> names = new HashSet<>();
    private static Map<String, Integer> depth = new HashMap<>();
    private static Map<String, String[]> up = new HashMap<>();
    private static final int MAX_LOG = 16;

    public static void main(String[] args) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("문제해결기법\\Assignment_29\\tree.out"));
        BufferedReader br = new BufferedReader(new FileReader("문제해결기법\\Assignment_29\\tree.inp"));
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
            childrenMap.putIfAbsent(parent, new ArrayList<>());
            childrenMap.get(parent).add(child);
        }

        for (String name : names) {
            if (!parentMap.containsKey(name)) {
                depth.put(name, 0);
                up.put(name, new String[MAX_LOG]);
                dfs(name, null);
            }
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

    private static void dfs(String node, String parent) {
        up.put(node, new String[MAX_LOG]);
        up.get(node)[0] = parent;

        for (int i = 1; i < MAX_LOG; i++) {
            if (up.get(node)[i - 1] != null) {
                up.get(node)[i] = up.get(up.get(node)[i - 1])[i - 1];
            } else {
                up.get(node)[i] = null;
            }
        }

        if (childrenMap.containsKey(node)) {
            for (String child : childrenMap.get(node)) {
                depth.put(child, depth.get(node) + 1);
                dfs(child, node);
            }
        }
    }

    private static String findLCA(String p, String q) {
        if (depth.get(p) < depth.get(q)) {
            String temp = p;
            p = q;
            q = temp;
        }

        int diff = depth.get(p) - depth.get(q);
        for (int i = 0; i < MAX_LOG; i++)
            if ((diff & (1 << i)) != 0)
                p = up.get(p)[i];

        if (p.equals(q))
            return p;

        for (int i = MAX_LOG - 1; i >= 0; i--) {
            if (up.get(p)[i] != null && !up.get(p)[i].equals(up.get(q)[i])) {
                p = up.get(p)[i];
                q = up.get(q)[i];
            }
        }

        return up.get(p)[0];
    }

    private static String findRelationship(String p, String q) {
        if (!names.contains(p) || !names.contains(q))
            return "no relation";
        if (parentMap.get(p) != null && parentMap.get(p).equals(parentMap.get(q)))
            return "sibling";
        if (parentMap.get(p) != null && parentMap.get(p).equals(q))
            return "child";
        if (parentMap.get(q) != null && parentMap.get(q).equals(p))
            return "parent";

        int pToQDescendant = getDescendantLevel(p, q);
        int qToPDescendant = getDescendantLevel(q, p);

        if (pToQDescendant > 0)
            return descendantString(pToQDescendant);
        if (qToPDescendant > 0)
            return ancestorString(qToPDescendant);

        String lca = findLCA(p, q);
        if (lca == null)
            return "no relation";

        int pDepth = depth.get(p);
        int qDepth = depth.get(q);
        int lcaDepth = depth.get(lca);

        int pDistance = pDepth - lcaDepth;
        int qDistance = qDepth - lcaDepth;

        if (lca.equals(parentMap.get(p)) || lca.equals(parentMap.get(q)))
            if (pDistance == 1 && qDistance == 1)
                return "sibling";

        int k = Math.min(pDistance, qDistance);
        int j = Math.abs(pDistance - qDistance);
        return (j == 0) ? (k - 1) + " cousin" : (k - 1) + " cousin removed " + j;
    }

    private static int getDescendantLevel(String child, String ancestor) {
        int level = 0;
        while (parentMap.containsKey(child)) {
            if (parentMap.get(child).equals(ancestor))
                return level + 1;
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

    private static String repeat(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++)
            sb.append(str);
        return sb.toString();
    }
}
