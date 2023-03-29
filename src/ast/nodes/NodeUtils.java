package ast.nodes;

import java.util.ArrayList;
import java.util.List;

public class NodeUtils {
    public static List<ASTNode> collectNodes(ASTNode ... nodes) {
        var collected = new ArrayList<ASTNode>();

        for (var node : nodes) {
            if (node != null)
                collected.add(node);
        }

        return collected;
    }
}
