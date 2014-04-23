package com.hwhl.rm.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hwhl.rm.model.RiskRelation;
import com.hwhl.rm.model.RiskRelation;
import com.hwhl.rm.util.StrUtil;

/**
 * 对目录文件进行解析
 */
public class RiskRelationDataParser {

	public List<RiskRelation> getItems(InputStream inStream) throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ItemParser itemParser = new ItemParser();
		parser.parse(inStream, itemParser);
		inStream.close();
		return itemParser.getItems();
	}

	private final class ItemParser extends DefaultHandler {
		private List<RiskRelation> items = null;
		private String tag = null;
		private RiskRelation item = null;
		private String lastTag="";

		public List<RiskRelation> getItems() {
			return items;
		}

		@Override
		public void startDocument() throws SAXException {
			items = new ArrayList<RiskRelation>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("Risk".equals(localName)) {
				item = new RiskRelation();
				// project.setId(new Integer(attributes.getValue(0)));
			}
			tag = localName;
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (tag != null) {
				String data = new String(ch, start, length);
				if ("id".equals(tag)) {
					if (lastTag.equals(tag))
						item.setId(StrUtil.nullToStr(item.getId()) + data);
					else
						item.setId(data);
					lastTag = tag;
				}  else if ("projectid".equals(tag)) {
					if (lastTag.equals(tag))
						item.setProjectid(StrUtil.nullToStr(item.getProjectid())
								+ data);
					else
						item.setProjectid(data);
					lastTag = tag;
				} else if ("riskTo".equals(tag)) {
					if (lastTag.equals(tag))
						item.setRiskTo(StrUtil.nullToStr(item.getRiskTo())
								+ data);
					else
						item.setRiskTo(data);
					lastTag = tag;
				}else if ("riskFrom".equals(tag)) {
					if (lastTag.equals(tag))
						item.setRiskTo(StrUtil.nullToStr(item.getRiskFrom())
								+ data);
					else
						item.setRiskFrom(data);
					lastTag = tag;
				} else if ("relationRemark".equals(tag)) {
					if (lastTag.equals(tag))
						item.setRelationRemark(StrUtil.nullToStr(item.getRelationRemark())
								+ data);
					else
						item.setRelationRemark(data);
					lastTag = tag;
				}  
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if ("Risk".equals(localName)) {
				items.add(item);
				item = null;
			}
			tag = null;
		}
	}
}
