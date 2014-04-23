package com.hwhl.rm.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hwhl.rm.model.Directory;
import com.hwhl.rm.util.StrUtil;

/**
 * 对目录文件进行解析
 */
public class DirectoryDataParser {

	public List<Directory> getItems(InputStream inStream) throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ItemParser itemParser = new ItemParser();
		parser.parse(inStream, itemParser);
		inStream.close();
		return itemParser.getDirectorys();
	}

	private final class ItemParser extends DefaultHandler {
		private List<Directory> items = null;
		private String tag = null;
		private Directory item = null;
		private String lastTag = "";

		public List<Directory> getDirectorys() {
			return items;
		}

		@Override
		public void startDocument() throws SAXException {
			items = new ArrayList<Directory>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("Risk".equals(localName)) {
				item = new Directory();
				// project.setId(new Integer(attributes.getValue(0)));
			}
			tag = localName;
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (tag != null) {
				String data = new String(ch, start, length);// 锟斤拷取锟侥憋拷锟节碉拷锟斤拷锟斤拷
				if ("id".equals(tag)) {
					if (lastTag.equals(tag))
						item.setId(StrUtil.nullToStr(item.getId()) + data);
					else
						item.setId(data);
					lastTag = tag;
				} else if ("title".equals(tag)) {
					if (lastTag.equals(tag))
						item.setTitle(StrUtil.nullToStr(item.getTitle()) + data);
					else
						item.setTitle(data);
					lastTag = tag;
				} else if ("fatherid".equals(tag)) {
					if (lastTag.equals(tag))
						item.setFatherid(StrUtil.nullToStr(item.getFatherid())
								+ data);
					else
						item.setFatherid(data);
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
