package com.hwhl.rm.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hwhl.rm.model.DicType;
import com.hwhl.rm.util.StrUtil;

/**
 * 对目录文件进行解析
 */
public class DicTypeDataParser {

	public List<DicType> getItems(InputStream inStream) throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ItemParser itemParser = new ItemParser();
		parser.parse(inStream, itemParser);
		inStream.close();
		return itemParser.getItems();
	}

	private final class ItemParser extends DefaultHandler {
		private List<DicType> items = null;
		private String tag = null;
		private DicType item = null;
		private String lastTag = "";

		public List<DicType> getItems() {
			return items;
		}

		@Override
		public void startDocument() throws SAXException {
			items = new ArrayList<DicType>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("Risk".equals(localName)) {
				item = new DicType();
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
				} else if ("fatherId".equals(tag)) {
					if (lastTag.equals(tag))
						item.setFatherId(StrUtil.nullToStr(item.getFatherId())
								+ data);
					else
						item.setFatherId(data);
					lastTag = tag;
				} else if ("title".equals(tag)) {
					if (lastTag.equals(tag))
						item.setTitle(StrUtil.nullToStr(item.getTitle()) + data);
					else
						item.setTitle(data);
					lastTag = tag;
				} else if ("typeStr".equals(tag)) {
					if (lastTag.equals(tag))
						item.setTypeStr(StrUtil.nullToStr(item.getTypeStr())
								+ data);
					else
						item.setTypeStr(data);
					lastTag = tag;
				} else if ("isDel".equals(tag)) {
					if (lastTag.equals(tag))
						item.setIsDel(StrUtil.nullToStr(item.getIsDel()) + data);
					else
						item.setIsDel(data);
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
