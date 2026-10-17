package cn.com.adtech.riss2.controller.common;

import java.beans.PropertyEditor;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import cn.com.adtech.riss2.bso.ICommonService;

import com.jbetter.spring.web.propertyeditors.DateEditor;


public abstract class AbsFormController extends SimpleFormController
{

	protected Object formBackingObject(HttpServletRequest arg0)
			throws Exception
	{
		supportData(arg0);
		return super.formBackingObject(arg0);
	}

	protected Object makeObject(String idName, Class objClass,
			HttpServletRequest request) throws Exception
	{
		Object obj = null;
		long id = ServletRequestUtils.getLongParameter(request, idName, 0);
		if (id != 0)
		{
			obj = getCommomService().get(objClass, new Long(id));
		} else
		{
			Long idl = (Long) request.getAttribute(idName);
			if (idl != null)
			{
				obj = getCommomService().get(objClass, idl);
			}
		}
		if (obj == null)
		{
			obj = objClass.newInstance();
		}
		return obj;
	}

	abstract protected void supportData(HttpServletRequest request);

	abstract protected void validate(HttpServletRequest request, Object obj,
			Errors errors);

	protected ModelAndView processFormSubmission(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, BindException arg3)
			throws Exception
	{
		validate(arg0, arg2, arg3);
		return super.processFormSubmission(arg0, arg1, arg2, arg3);
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception
	{
		super.initBinder(request, binder);
		// 设成Number可为空
		PropertyEditor shortEditor = new CustomNumberEditor(Short.class, true);
		PropertyEditor integerEditor = new CustomNumberEditor(Integer.class,
				true);
		PropertyEditor longEditor = new CustomNumberEditor(Long.class, true);
		PropertyEditor floatEditor = new CustomNumberEditor(Float.class, true);
		PropertyEditor doubleEditor = new CustomNumberEditor(Double.class, true);

		binder.registerCustomEditor(Short.class, shortEditor);
		binder.registerCustomEditor(Integer.class, integerEditor);
		binder.registerCustomEditor(Long.class, longEditor);
		binder.registerCustomEditor(Float.class, floatEditor);
		binder.registerCustomEditor(Double.class, doubleEditor);
		binder.registerCustomEditor(Date.class, new DateEditor());
	}

	protected Date validateDate(String value, String colName, String colEName,
			Errors errors)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date result = null;
		try
		{
			result = dateFormat.parse(value);
		} catch (Exception e)
		{
			errors.rejectValue(colEName, null, "“" + colName
					+ "”填写格式不正确，请按照“年-月-日的格式”如:“1997-06-08”");
		}
		return result;
	}
	
	private ICommonService commomService;

	public ICommonService getCommomService() {
		return commomService;
	}

	public void setCommomService(ICommonService commomService) {
		this.commomService = commomService;
	}
}
