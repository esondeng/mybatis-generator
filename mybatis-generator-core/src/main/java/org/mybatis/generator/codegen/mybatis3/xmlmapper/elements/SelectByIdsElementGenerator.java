package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @author dengxiaolin
 * @since 2021/05/20
 */
public class SelectByIdsElementGenerator extends AbstractXmlElementGenerator {
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer.addAttribute(new Attribute("id", introspectedTable.getSelectByPrimaryKeysStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));
        context.getCommentGenerator().addComment(answer);

        answer.addElement(new TextElement("select "));
        answer.addElement(getBaseColumnListElement());

        StringBuilder sb = new StringBuilder();
        sb.append("from ");
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
        // 只支持一个主键，不支持组合主键
        IntrospectedColumn primaryKey = introspectedColumns.get(0);
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

        parentElement.addElement(answer);
        parentElement.addElement(new TextElement(""));
    }

    @Override
    protected XmlElement getBaseColumnListElement() {
        XmlElement answer = new XmlElement("include");
        answer.addAttribute(new Attribute("refid",
                introspectedTable.getBaseColumnListId()));
        return answer;
    }
}
