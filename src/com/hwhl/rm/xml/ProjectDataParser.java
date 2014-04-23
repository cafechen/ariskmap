package com.hwhl.rm.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hwhl.rm.model.Project;
import com.hwhl.rm.util.StrUtil;

/**
 * 锟斤拷锟斤拷SAX锟斤拷锟斤拷XML锟斤拷锟斤拷
 */
public class ProjectDataParser {

	public List<Project> getProjects(InputStream inStream) throws Throwable {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		ProjectParser projectParser = new ProjectParser();
		parser.parse(inStream, projectParser);
		inStream.close();
		return projectParser.getProjects();
	}

	private final class ProjectParser extends DefaultHandler {
		private List<Project> projects = null;
		private String tag = null;
		private Project project = null;
		private String lastTag = "";

		public List<Project> getProjects() {
			return projects;
		}

		@Override
		public void startDocument() throws SAXException {
			projects = new ArrayList<Project>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("Risk".equals(localName)) {
				project = new Project();
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
						project.setId(StrUtil.nullToStr(project.getId()) + data);
					else
						project.setId(data);
					lastTag = tag;
				} else if ("fatherId".equals(tag)) {
					if (lastTag.equals(tag))
						project.setFatherId(StrUtil.nullToStr(project
								.getFatherId()) + data);
					else
						project.setFatherId(data);
					lastTag = tag;
				} else if ("belong_department".equals(tag)) {
					if (lastTag.equals(tag))
						project.setBelong_department(StrUtil.nullToStr(project
								.getBelong_department()) + data);
					else
						project.setBelong_department(data);
					lastTag = tag;

				} else if ("title".equals(tag)) {
					if (lastTag.equals(tag))
						project.setTitle(StrUtil.nullToStr(project.getTitle())
								+ data);
					else
						project.setTitle(data);
					lastTag = tag;

				} else if ("isUpload".equals(tag)) {
					if (lastTag.equals(tag))
						project.setIsUpload(StrUtil.nullToStr(project
								.getIsUpload()) + data);
					else
						project.setIsUpload(data);
					lastTag = tag;

				} else if ("AddDate".equals(tag)) {
					if (lastTag.equals(tag))
						project.setAddDate(StrUtil.nullToStr(project
								.getAddDate()) + data);
					else
						project.setAddDate(data);
					lastTag = tag;

				} else if ("isComplete".equals(tag)) {
					if (lastTag.equals(tag))
						project.setIsComplete(StrUtil.nullToStr(project
								.getIsComplete()) + data);
					else
						project.setIsComplete(data);
					lastTag = tag;

				} else if ("show_card".equals(tag)) {
					if (lastTag.equals(tag))
						project.setShow_card(StrUtil.nullToStr(project
								.getShow_card()) + data);
					else
						project.setShow_card(data);
					lastTag = tag;

				} else if ("show_hot".equals(tag)) {
					if (lastTag.equals(tag))
						project.setShow_hot(StrUtil.nullToStr(project
								.getShow_hot()) + data);
					else
						project.setShow_hot(data);
					lastTag = tag;

				} else if ("show_chengben".equals(tag)) {
					if (lastTag.equals(tag))
						project.setShow_chengben(StrUtil.nullToStr(project
								.getShow_chengben()) + data);
					else
						project.setShow_chengben(data);
					lastTag = tag;

				} else if ("show_static".equals(tag)) {
					if (lastTag.equals(tag))
						project.setShow_static(StrUtil.nullToStr(project
								.getShow_static()) + data);
					else
						project.setShow_static(data);
					lastTag = tag;

				} else if ("show_after".equals(tag)) {
					if (lastTag.equals(tag))
						project.setShow_after(StrUtil.nullToStr(project
								.getShow_after()) + data);
					else
						project.setShow_after(data);
					lastTag = tag;
				} else if ("projectid".equals(tag)) {
					if (lastTag.equals(tag))
						project.setProjectid(StrUtil.nullToStr(project
								.getProjectid()) + data);
					else
						project.setProjectid(data);
					lastTag = tag;

				} else if ("remark".equals(tag)) {
					if (lastTag.equals(tag))
						project.setRemark(StrUtil.nullToStr(project.getRemark())
								+ data);
					else
						project.setRemark(data);
					lastTag = tag;

				} else if ("show_sort".equals(tag)) {
					if (lastTag.equals(tag))
						project.setShow_sort(StrUtil.nullToStr(project
								.getShow_sort()) + data);
					else
						project.setShow_sort(data);
					lastTag = tag;

				}else if ("huobi".equals(tag)) {
					if (lastTag.equals(tag))
						project.setHuobi(StrUtil.nullToStr(project
								.getHuobi()) + data);
					else
						project.setHuobi(data);
					lastTag = tag;

				}
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if ("Risk".equals(localName)) {
				projects.add(project);
				project = null;
			}
			tag = null;
		}
	}
}
