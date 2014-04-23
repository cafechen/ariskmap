package com.hwhl.rm.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hwhl.rm.model.ProjectVectorDetail;
import com.hwhl.rm.util.StrUtil;

/**
 * 对目录文件进行解析
 */
public class ProjectVectorDetailDataParser {

	public List<ProjectVectorDetail> getItems(InputStream inStream)
			throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ItemParser itemParser = new ItemParser();
		parser.parse(inStream, itemParser);
		inStream.close();
		return itemParser.getItems();
	}

	private final class ItemParser extends DefaultHandler {
		private List<ProjectVectorDetail> items = null;
		private String tag = null;
		private ProjectVectorDetail item = null;
		private String lastTag = "";

		public List<ProjectVectorDetail> getItems() {
			return items;
		}

		@Override
		public void startDocument() throws SAXException {
			items = new ArrayList<ProjectVectorDetail>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("Risk".equals(localName)) {
				item = new ProjectVectorDetail();
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
				} else if ("fatherid".equals(tag)) {
					if (lastTag.equals(tag))
						item.setFatherid(StrUtil.nullToStr(item.getFatherid())
								+ data);
					else
						item.setFatherid(data);
					lastTag = tag;
				} else if ("levelTitle".equals(tag)) {
					if (lastTag.equals(tag))
						item.setLevelTitle(StrUtil.nullToStr(item
								.getLevelTitle()) + data);
					else
						item.setLevelTitle(data);
					lastTag = tag;
				} else if ("score".equals(tag)) {
					if (lastTag.equals(tag))
						item.setScore(StrUtil.nullToStr(item.getScore()) + data);
					else
						item.setScore(data);
					lastTag = tag;
				} else if ("remarkTitle".equals(tag)) {
					if (lastTag.equals(tag))
						item.setRemarkTitle(StrUtil.nullToStr(item
								.getRemarkTitle()) + data);
					else
						item.setRemarkTitle(data);
					lastTag = tag;
				} else if ("remarkContent".equals(tag)) {
					if (lastTag.equals(tag))
						item.setRemarkContent(StrUtil.nullToStr(item
								.getRemarkContent()) + data);
					else
						item.setRemarkContent(data);
					lastTag = tag;
				} else if ("theType".equals(tag)) {
					if (lastTag.equals(tag))
						item.setTheType(StrUtil.nullToStr(item.getTheType())
								+ data);
					else
						item.setTheType(data);
					lastTag = tag;
				} else if ("projectId".equals(tag)) {
					if (lastTag.equals(tag))
						item.setProjectId(StrUtil.nullToStr(item.getProjectId())
								+ data);
					else
						item.setProjectId(data);
					lastTag = tag;
				} else if ("sort".equals(tag)) {
					if (lastTag.equals(tag))
						item.setSort(StrUtil.nullToStr(item.getSort()) + data);
					else
						item.setSort(data);
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
