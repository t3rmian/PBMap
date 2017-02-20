package io.github.t3r1jj.pbmap.model;

import android.content.Context;
import android.widget.ImageView;

import org.simpleframework.xml.Attribute;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import io.github.t3r1jj.pbmap.view.PlaceView;
import io.github.t3r1jj.pbmap.view.SpaceView;

public class Space extends Place {
    @Attribute(name = "reference_map_path", required = false)
    protected String referenceMapPath;
    @Attribute(name = "description_res_name", required = false)
    protected String descriptionResName;

    @Override
    public PlaceView createView(Context context) {
        return new SpaceView(context, this);
    }

    public String getReferenceMapPath() {
        return referenceMapPath;
    }

    public void setReferenceMapPath(String referenceMapPath) {
        this.referenceMapPath = referenceMapPath;
    }

    public String getDescriptionResName() {
        return descriptionResName;
    }

    public ImageView getLogo(Context context) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(context.getAssets().open(referenceMapPath));
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = xpath.compile("element/@reference_map_path");
        NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
    }
}
