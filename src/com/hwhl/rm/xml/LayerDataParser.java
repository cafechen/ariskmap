package com.hwhl.rm.xml;

import com.hwhl.rm.model.Layer;
import com.hwhl.rm.model.RiskRelation;
import com.hwhl.rm.util.StrUtil;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 对目录文件进行解析
 */
public class LayerDataParser {

	public List<Layer> getItems(InputStream inStream) throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ItemParser itemParser = new ItemParser();
		parser.parse(inStream, itemParser);
		inStream.close();
		return itemParser.getItems();
	}

	private final class ItemParser extends DefaultHandler {
		private List<Layer> items = null;
		private String tag = null;
		private Layer item = null;
		private String lastTag="";

		public List<Layer> getItems() {
			return items;
		}

		@Override
		public void startDocument() throws SAXException {
			items = new ArrayList<Layer>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("Risk".equals(localName)) {
				item = new Layer();
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
				}  else if ("layerName".equals(tag)) {
					if (lastTag.equals(tag))
						item.setLayerName(StrUtil.nullToStr(item.getLayerName())
								+ data);
					else
						item.setLayerName(data);
					lastTag = tag;
				} else if ("visible".equals(tag)) {
                    if (lastTag.equals(tag))
                        item.setVisible(StrUtil.nullToStr(item.getVisible())
                                + data);
                    else
                        item.setVisible(data);
                    lastTag = tag;
                } else if ("projectId".equals(tag)) {
                    if (lastTag.equals(tag))
                        item.setProjectId(StrUtil.nullToStr(item.getProjectId())
                                + data);
                    else
                        item.setProjectId(data);
                    lastTag = tag;
                } else if ("pageIndex".equals(tag)) {
                    if (lastTag.equals(tag))
                        item.setPageIndex(StrUtil.nullToStr(item.getPageIndex())
                                + data);
                    else
                        item.setPageIndex(data);
                    lastTag = tag;
                } else if ("belongpage".equals(tag)) {
                    if (lastTag.equals(tag))
                        item.setBelongPage(StrUtil.nullToStr(item.getBelongPage())
                                + data);
                    else
                        item.setBelongPage(data);
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
