package com.hwhl.rm.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hwhl.rm.model.ProjectMatrix;
import com.hwhl.rm.util.StrUtil;

/**
 * 对目录文件进行解析
 */
public class ProjectMatrixDataParser {

	public List<ProjectMatrix> getItems(InputStream inStream) throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ItemParser itemParser = new ItemParser();
		parser.parse(inStream, itemParser);
		inStream.close();
		return itemParser.getItems();
	}

	private final class ItemParser extends DefaultHandler {
		private List<ProjectMatrix> items = null;
		private String tag = null;
		private ProjectMatrix item = null;
		private String lastTag = "";

		public List<ProjectMatrix> getItems() {
			return items;
		}

		@Override
		public void startDocument() throws SAXException {
			items = new ArrayList<ProjectMatrix>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("Risk".equals(localName)) {
				item = new ProjectMatrix();
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
				} else if ("matrix_title".equals(tag)) {
					if (lastTag.equals(tag))
						item.setMatrix_title(StrUtil.nullToStr(item
								.getMatrix_title()) + data);
					else
						item.setMatrix_title(data);
					lastTag = tag;
				} else if ("matrix_x".equals(tag)) {
					if (lastTag.equals(tag))
						item.setMatrix_x(StrUtil.nullToStr(item.getMatrix_x())
								+ data);
					else
						item.setMatrix_x(data);
					lastTag = tag;
				} else if ("matrix_y".equals(tag)) {
					if (lastTag.equals(tag))
						item.setMatrix_y(StrUtil.nullToStr(item.getMatrix_y())
								+ data);
					else
						item.setMatrix_y(data);
					lastTag = tag;
				} else if ("fatherid_matrix".equals(tag)) {
					if (lastTag.equals(tag))
						item.setFatherid_matrix(StrUtil.nullToStr(item
								.getFatherid_matrix()) + data);
					else
						item.setFatherid_matrix(data);
					lastTag = tag;
				} else if ("xIndex".equals(tag)) {
					if (lastTag.equals(tag))
						item.setxIndex(StrUtil.nullToStr(item.getxIndex())
								+ data);
					else
						item.setxIndex(data);
					lastTag = tag;
				} else if ("yIndex".equals(tag)) {
					if (lastTag.equals(tag))
						item.setyIndex(StrUtil.nullToStr(item.getyIndex())
								+ data);
					else
						item.setyIndex(data);
					lastTag = tag;
				} else if ("Color".equals(tag)) {
					if (lastTag.equals(tag))
						item.setColor(StrUtil.nullToStr(item.getColor()) + data);
					else
						item.setColor(data);
					lastTag = tag;
				} else if ("levelType".equals(tag)) {
					if (lastTag.equals(tag))
						item.setLevelType(StrUtil.nullToStr(item.getLevelType())
								+ data);
					else
						item.setLevelType(data);
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
