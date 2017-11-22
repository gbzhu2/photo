package org.lilystudio.smarty4j.statement.function;

import static org.objectweb.asm.Opcodes.*;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lilystudio.smarty4j.Template;
import org.lilystudio.smarty4j.TemplateReader;
import org.lilystudio.smarty4j.statement.Function;
import org.lilystudio.smarty4j.statement.ParameterCharacter;
import org.lilystudio.smarty4j.statement.ParentType;
import org.objectweb.asm.MethodVisitor;

/**
 * 参见foreach函数。
 * 
 * @see org.lilystudio.smarty4j.statement.function.$foreach
 * 
 * @version 1.0.1, 2014/03/19
 * @author 欧阳先伟
 * @modify simon4545
 * @since Smarty 1.0
 */
@ParentType(name = "foreach")
public class $foreachelse extends Function {

	/** 文本缓冲区 */
	private StringBuilder text = new StringBuilder();
	/** 结束符 */
	private static Pattern p = Pattern.compile("\\{ */ *foreach *\\}");

	public ParameterCharacter[] getDefinitions() {
		return null;
	}

	@Override
	public void process(Template template, TemplateReader in, String left,
			String right) {
		while (true) {
			String line;
			try {
				line = in.readLine();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}

			if (line == null) {
				in.addMessage("没有找到foreach的结束标签");
				return;
			}

			Matcher m = p.matcher(line);
			if (m.find()) {
				int mStart=m.start();
				text.append(line.substring(0, mStart));
				//simon:还需要把foreach的结束标志让出来
				in.unread(line.substring(mStart));
				return;
			}

			text.append(line);
		}
	}

	public void parse(MethodVisitor mw, int local,
			Map<String, Integer> variableNames) {
		mw.visitVarInsn(ALOAD, WRITER);
		mw.visitLdcInsn(text.toString());
		mw.visitMethodInsn(INVOKEVIRTUAL, "java/io/Writer", "write",
				"(Ljava/lang/String;)V");
	}
}
