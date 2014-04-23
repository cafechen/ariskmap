package com.hwhl.rm.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hwhl.rm.model.ProjectVector;
import com.hwhl.rm.util.StrUtil;

/**
 * 对目录文件进行解析
 */
public class ProjectVectorDataParser {

	public List<ProjectVector> getItems(InputStream inStream) throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ItemParser itemParser = new ItemParser();
		parser.parse(inStream, itemParser);
		inStream.close();
		return itemParser.getItems();
	}

	private final class ItemParser extends DefaultHandler {
		private List<ProjectVector> items = null;
		private String tag = null;
		private ProjectVector item = null;
		private String lastTag = "";

		public List<ProjectVector> getItems() {
			return items;
		}

		@Override
		public void startDocument() throws SAXException {
			items = new ArrayList<ProjectVector>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("Risk".equals(localName)) {
				item = new ProjectVector();
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
				} else if ("title".equals(tag)) {
					if (lastTag.equals(tag))
						item.setTitle(StrUtil.nullToStr(item.getTitle()) + data);
					else
						item.setTitle(data);
					lastTag = tag;
				} else if ("remark".equals(tag)) {
					if (lastTag.equals(tag))
						item.setRemark(StrUtil.nullToStr(item.getRemark())
								+ data);
					else
						item.setRemark(data);
					lastTag = tag;
				} else if ("theType".equals(tag)) {
					if (lastTag.equals(tag))
						item.setTheType(StrUtil.nullToStr(item.getTheType())
								+ data);
					else
						item.setTheType(data);
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
