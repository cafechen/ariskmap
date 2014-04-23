package com.hwhl.rm.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.hwhl.rm.model.RiskScore;
import com.hwhl.rm.util.StrUtil;

/**
 * 对目录文件进行解析
 */
public class RiskScoreDataParser {

	public List<RiskScore> getItems(InputStream inStream) throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ItemParser itemParser = new ItemParser();
		parser.parse(inStream, itemParser);
		inStream.close();
		return itemParser.getItems();
	}

	private final class ItemParser extends DefaultHandler {
		private List<RiskScore> items = null;
		private String tag = null;
		private RiskScore item = null;
		private String lastTag="";

		public List<RiskScore> getItems() {
			return items;
		}

		@Override
		public void startDocument() throws SAXException {
			items = new ArrayList<RiskScore>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("Risk".equals(localName)) {
				item = new RiskScore();
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
				} else if ("riskid".equals(tag)) {
					if (lastTag.equals(tag))
						item.setRiskid(StrUtil.nullToStr(item.getRiskid())
								+ data);
					else
						item.setRiskid(data);
					lastTag = tag;
				} else if ("scoreVectorId".equals(tag)) {
					if (tag == lastTag)
						item.setScoreVectorId(StrUtil.nullToStr(item
								.getScoreVectorId()) + data);
					else
						item.setScoreVectorId(data);
					lastTag = tag;
				} else if ("scoreBefore".equals(tag)) {
					if (lastTag.equals(tag))
						item.setScoreBefore(StrUtil.nullToStr(item
								.getScoreBefore()) + data);
					else
						item.setScoreBefore(data);
					lastTag = tag;
				} else if ("scoreEnd".equals(tag)) {
					if (lastTag.equals(tag))
						item.setScoreEnd(StrUtil.nullToStr(item.getScoreEnd())
								+ data);
					else
						item.setScoreEnd(data);
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
