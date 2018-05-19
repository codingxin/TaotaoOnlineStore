package com.taotao.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreenarker {
	@Test
	public void testFreemarker() throws Exception {
		// 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
		Configuration configuration = new Configuration(Configuration.getVersion());
		// 第二步：设置模板文件所在的路径。
		configuration.setDirectoryForTemplateLoading(
				new File("F:/TaotaoOnlineStore/taotao-item-web/src/main/webapp/WEB-INF/ftl"));
		// 第三步：设置模板文件使用的字符集。一般就是utf-8.
		configuration.setDefaultEncoding("utf-8");
		// 第四步：加载一个模板，创建一个模板对象。
		// Template template = configuration.getTemplate("hello.ftl");
		Template template = configuration.getTemplate("student.ftl");
		// 第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
		Map dataModel = new HashMap<>();
		Student student = new Student(1, "小米", 11, "长沙");
		dataModel.put("student", student);
		// 向数据集中添加数据
		dataModel.put("hello", "this is my first freemarker test.");
		
		List<Student> studentslist=new ArrayList<Student>();
		studentslist.add(new Student(1, "小米", 11, "长沙"));
		studentslist.add(new Student(2, "小米", 11, "长沙"));
		studentslist.add(new Student(3, "小米", 11, "长沙"));
		studentslist.add(new Student(4, "小米", 11, "长沙"));
		studentslist.add(new Student(5, "小米", 11, "长沙"));
		// 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
		Writer out = new FileWriter(new File("F:/temp/student.html"));
		// 第七步：调用模板对象的process方法输出文件。
		template.process(studentslist, out);
		// 第八步：关闭流。
		out.close();

	}

}
