package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * @author dengxiaolin
 * @since 2021/05/21
 */
public class AddDeleteByIdsElementGenerator extends AbstractXmlElementGenerator {
    @Override
    public void addElements(XmlElement parentElement) {
        boolean hasValidColumn = introspectedTable.getColumn("valid").isPresent();
        boolean hasUsableColumn = introspectedTable.getColumn("is_usable").isPresent();
        boolean hasUsableStateColumn = introspectedTable.getColumn("usable_state").isPresent();

        if (!hasValidColumn && !hasUsableColumn) {
            return;
        }

        XmlElement answer = new XmlElement("update");

        answer.addAttribute(new Attribute("id", introspectedTable.getDeleteByIdsStatementId()));
        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        boolean isHasValidColumn = introspectedTable.getColumn("valid").isPresent();
        boolean hasLastUpdater = introspectedTable.getColumn("last_updater").isPresent();
        boolean hasLastUpdateTime = introspectedTable.getColumn("last_update_time").isPresent();

        if (isHasValidColumn) {
            sb.append(" set valid = 0, ");
        }
        if (hasUsableColumn) {
            sb.append(" set is_usable = 0, ");
        }
        if (hasUsableStateColumn) {
            sb.append(" set usable_state = 0, ");
        }

        if (hasLastUpdater) {
            String jdbcType = introspectedTable.getColumn("last_updater").get().getJdbcTypeName();
            sb.append("hasLastUpdater = #{hasLastUpdater,jdbcType=").append(jdbcType).append("}, ");
        }

        if (hasLastUpdateTime) {
            sb.append("last_update_time = now()");
        }
        else {
            sb.append("update_time = now()");
        }
        answer.addElement(new TextElement(sb.toString()));

        // only support one column primary key
        IntrospectedColumn primaryKey = introspectedTable.getPrimaryKeyColumns().get(0);
        answer.addElement(new TextElement("where " + primaryKey.getActualColumnName() + " in "));

        String collectionName = primaryKey.getJavaProperty() + "s";
        XmlElement forEachElement = new XmlElement("foreach");
        forEachElement.addAttribute(new Attribute("collection", collectionName));
        forEachElement.addAttribute(new Attribute("item", primaryKey.getJavaProperty()));
        forEachElement.addAttribute(new Attribute("open", "("));
        forEachElement.addAttribute(new Attribute("close", ")"));
        forEachElement.addAttribute(new Attribute("index", "index"));
        forEachElement.addAttribute(new Attribute("separator", ","));

        forEachElement.addElement(new TextElement(MyBatis3FormattingUtilities.getParameterClause(primaryKey, null)));
        answer.addElement(forEachElement);
        if (isHasValidColumn) {
            answer.addElement(new TextElement(" and valid = 1"));
        }
        if (hasUsableColumn) {
            answer.addElement(new TextElement(" and is_usable = 1"));
        }
        if (hasUsableStateColumn) {
            answer.addElement(new TextElement(" and usable_state = 1"));
        }


        parentElement.addElement(answer);
        parentElement.addElement(new TextElement(""));
    }
}
