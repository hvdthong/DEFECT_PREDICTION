package org.apache.camel.component.file.strategy;

import java.util.Map;

import org.apache.camel.Expression;
import org.apache.camel.component.file.FileProcessStrategy;

/**
 * Factory to provide the {@link org.apache.camel.component.file.FileProcessStrategy} to use.
 */
public final class FileProcessStrategyFactory {

    private FileProcessStrategyFactory() {
    }

    /**
     * A strategy method to lazily create the file strategy to use.
     */
    public static FileProcessStrategy createFileProcessStrategy(Map<String, Object> params) {

        boolean isDelete = params.get("delete") != null;
        boolean isLock = params.get("lock") != null;
        String moveNamePrefix = (String) params.get("moveNamePrefix");
        String moveNamePostfix = (String) params.get("moveNamePostfix");
        String preMoveNamePrefix = (String) params.get("preMoveNamePrefix");
        String preMoveNamePostfix = (String) params.get("preMoveNamePostfix");
        Expression expression = (Expression) params.get("expression");
        Expression preMoveExpression = (Expression) params.get("preMoveExpression");
        boolean move = moveNamePrefix != null || moveNamePostfix != null;
        boolean preMove = preMoveNamePrefix != null || preMoveNamePostfix != null;

        if (params.containsKey("noop")) {
            return new NoOpFileProcessStrategy(isLock);
        } else if (move || preMove) {
            RenameFileProcessStrategy strategy = new RenameFileProcessStrategy(isLock);
            if (move) {
                strategy.setCommitRenamer(new DefaultFileRenamer(moveNamePrefix, moveNamePostfix));
            }
            if (preMove) {
                strategy.setBeginRenamer(new DefaultFileRenamer(preMoveNamePrefix, preMoveNamePostfix));
            }
            return strategy;
        } else if (expression != null || preMoveExpression != null) {
            RenameFileProcessStrategy strategy = new RenameFileProcessStrategy(isLock);
            if (expression != null) {
                FileExpressionRenamer renamer = new FileExpressionRenamer();
                renamer.setExpression(expression);
                strategy.setCommitRenamer(renamer);
            }
            if (preMoveExpression != null) {
                FileExpressionRenamer renamer = new FileExpressionRenamer();
                renamer.setExpression(preMoveExpression);
                strategy.setBeginRenamer(renamer);
            }
            return strategy;
        } else if (isDelete) {
            return new DeleteFileProcessStrategy(isLock);
        } else {
            return new RenameFileProcessStrategy(isLock);
        }
    }
}
