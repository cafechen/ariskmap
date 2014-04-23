package com.hwhl.rm.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hwhl.rm.model.RiskScoreFather;
import com.hwhl.rm.util.StrUtil;

/**
 * 对目录文件进行解析
 */
public class RiskScoreFatherDataParser {

	public List<RiskScoreFather> getItems(InputStream inStream) throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ItemParser itemParser = new ItemParser();
		parser.parse(inStream, itemParser);
		inStream.close();
		return itemParser.getItems();
	}

	private final class ItemParser extends DefaultHandler {
		private List<RiskScoreFather> items = null;
		private String tag = null;
		private RiskScoreFather item = null;
		private String lastTag="";

		public List<RiskScoreFather> getItems() {
			return items;
		}

		@Override
		public void startDocument() throws SAXException {
			items = new ArrayList<RiskScoreFather>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("Risk".equals(localName)) {
				item = new RiskScoreFather();
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
				} else if ("riskId".equals(tag)) {
					if (lastTag.equals(tag))
						item.setRiskId(StrUtil.nullToStr(item.getRiskId())
								+ data);
					else
						item.setRiskId(data);
					lastTag = tag;
				} else if ("before".equals(tag)) {
					if (tag == lastTag)
						item.setBefore(StrUtil.nullToStr(item
								.getBefore()) + data);
					else
						item.setBefore(data);
					lastTag = tag;
				} else if ("Send".equals(tag)) {
					if (lastTag.equals(tag))
						item.setSend(StrUtil.nullToStr(item
								.getSend()) + data);
					else
						item.setSend(data);
					lastTag = tag;
				} else if ("projectId".equals(tag)) {
					if (lastTag.equals(tag))
						item.setProjectId(StrUtil.nullToStr(item.getProjectId())
								+ data);
					else
						item.setProjectId(data);
					lastTag = tag;
				}
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if ("Risk".equals(localName)) {

				/*
				 * Log.i("System", StrUtil.nullToStr(item.getId())+"========="+
				 * StrUtil.nullToStr(item.getRiskid())+"========="+
				 * StrUtil.nullToStr(item.getScoreVectorId())+"========="+
				 * StrUtil.nullToStr(item.getScoreBefore())+"========="+
				 * StrUtil.nullToStr(item.getScoreEnd()));
				 */
				items.add(item);
				item = null;
			}
			tag = null;
		}
	}
}
