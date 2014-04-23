package com.hwhl.rm.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hwhl.rm.model.Risk;
import com.hwhl.rm.util.StrUtil;

/**
 * 对目录文件进行解析
 */
public class RiskDataParser {

	public List<Risk> getItems(InputStream inStream) throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ItemParser itemParser = new ItemParser();
		parser.parse(inStream, itemParser);
		inStream.close();
		return itemParser.getItems();
	}

	private final class ItemParser extends DefaultHandler {
		private List<Risk> items = null;
		private String tag = null;
		private Risk item = null;
		private String lastTag = "";

		public List<Risk> getItems() {
			return items;
		}

		@Override
		public void startDocument() throws SAXException {
			items = new ArrayList<Risk>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("Risk".equals(localName)) {
				item = new Risk();
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
				} else if ("projectId".equals(tag)) {
					if (lastTag.equals(tag))
						item.setProjectId(StrUtil.nullToStr(item.getProjectId())
								+ data);
					else
						item.setProjectId(data);
					lastTag = tag;
				} else if ("pageDetailId".equals(tag)) {
					if (lastTag.equals(tag))
						item.setPageDetailId(StrUtil.nullToStr(item
								.getPageDetailId()) + data);
					else
						item.setPageDetailId(data);
					lastTag = tag;
				} else if ("riskTitle".equals(tag)) {
					if (lastTag.equals(tag))
						item.setRiskTitle(StrUtil.nullToStr(item.getRiskTitle())
								+ data);
					else
						item.setRiskTitle(data);
					lastTag = tag;
				} else if ("riskCode".equals(tag)) {
					if (lastTag.equals(tag))
						item.setRiskCode(StrUtil.nullToStr(item.getRiskCode())
								+ data);
					else
						item.setRiskCode(data);
					lastTag = tag;
				} else if ("riskTypeId".equals(tag)) {
					if (lastTag.equals(tag))
						item.setRiskTypeId(StrUtil.nullToStr(item
								.getRiskTypeId()) + data);
					else
						item.setRiskTypeId(data);
					lastTag = tag;
				} else if ("riskTypeStr".equals(tag)) {
					if (lastTag.equals(tag))
						item.setRiskTypeStr(StrUtil.nullToStr(item
								.getRiskTypeStr()) + data);
					else
						item.setRiskTypeStr(data);
					lastTag = tag;
				} else if ("pageId".equals(tag)) {
					if (lastTag.equals(tag))
						item.setPageId(StrUtil.nullToStr(item.getPageId())
								+ data);
					else
						item.setPageId(data);
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
