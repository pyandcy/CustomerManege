package web.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import domain.Customer;
import domain.PageBean;
import service.CustomerService;
import cn.itcast.servlet.BaseServlet;

/**
 * Web层
 * @author cxf
 *
 */
public class CustomerServlet extends BaseServlet {
	private CustomerService customerService = new CustomerService();

	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Customer customer = CommonUtils.toBean(request.getParameterMap(), Customer.class);
		customer.setCid(CommonUtils.uuid());
		customerService.add(customer);
		request.setAttribute("msg","恭喜，添加数据成功！");
		return "f:/msg.jsp";
	}

	/**
	 * 添加客户
	 */
//	public String add(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		/*
//		 * 1. 封装表单数据到Customer对象
//		 * 2. 补全：cid，使用uuid
//		 * 3. 使用service方法完成添加工作
//		 * 4. 向request域中保存成功信息
//		 * 5. 转发到msg.jsp
//		 */
//		Customer c = CommonUtils.toBean(request.getParameterMap(), Customer.class);
//		c.setCid(CommonUtils.uuid());
//		customerService.add(c);
//		request.setAttribute("msg", "恭喜，添加客户成功！");
//		return "f:/msg.jsp";
//	}

//	public String findAll(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		request.setAttribute("cstmList",customerService.findAll());
//		return "f:/list.jsp";
//	}
//	public String findAll(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		int pc = getPc(request);
//		int ps=10;
//		PageBean<Customer> pb=customerService.findAll(pc,ps);
//		pb.setUrl(getUrl(request));
//		request.setAttribute("pb",pb);
//		return "f:/list.jsp";
//	}
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int pc=getPc(request);
		int ps=10;
		PageBean<Customer> pb = customerService.findAll(pc, ps);
		pb.setUrl(getUrl(request));
		request.setAttribute("pb",pb);
		return "f:/list.jsp";
	}

	public String query(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		System.out.println(getUrl(request));
		Customer criteria = CommonUtils.toBean(request.getParameterMap(), Customer.class);

//		处理get编码方式
		criteria=encoding(criteria);
		int pc=getPc(request);
		int ps=10;
		PageBean<Customer> pb=customerService.query(criteria,pc,ps);
		pb.setUrl(getUrl(request));
		request.setAttribute("pb",pb);
		return "f:/list.jsp";
	}

	private String getUrl(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		String servletPath = request.getServletPath();
		String queryString = request.getQueryString();
		if (queryString.contains("&pc=")){
			int index = queryString.lastIndexOf("&pc=");
			 queryString = queryString.substring(0, index);
		}
		return contextPath+servletPath+"?"+queryString;
	}

	private int getPc(HttpServletRequest request) {
		String pc = request.getParameter("pc");
		if (pc==null||pc.trim().length()==0){
			return 1;
		}
		return Integer.parseInt(pc);
	}

	private Customer encoding(Customer criteria) throws UnsupportedEncodingException {
		String cname = criteria.getCname();
		String gender=criteria.getGender();
		String cellphone = criteria.getCellphone();
		String description = criteria.getDescription();
		if (cname!=null&&!cname.trim().isEmpty()){
			cname= new String(cname.getBytes("ISO-8859-1"), "utf-8");
			criteria.setCname(cname);
		}
		if (gender!=null&&!gender.trim().isEmpty()){
			gender= new String(gender.getBytes("ISO-8859-1"), "utf-8");
			criteria.setGender(gender);
		}
		if (cellphone!=null&&!cellphone.trim().isEmpty()){
			cellphone= new String(cellphone.getBytes("ISO-8859-1"), "utf-8");
			criteria.setCellphone(cellphone);
		}
		if (description!=null&&!description.trim().isEmpty()){
			cname= new String(description.getBytes("ISO-8859-1"), "utf-8");
			criteria.setDescription(cname);
		}
		return criteria;
	}

//	public int getPc(HttpServletRequest request){
////		String pc = request.getParameter("pc");
////		if (pc==null||pc.trim().isEmpty()){
////			return 1;
////		}
////		return Integer.parseInt(pc);
////	}
//	private String getUrl(HttpServletRequest request){
//		String contextPath = request.getContextPath();
//		String servletPath = request.getServletPath();
//		String queryString = request.getQueryString();
//		if(queryString.contains("&pc=")){
//			int index = queryString.lastIndexOf("&pc=");
//			 queryString = queryString.substring(0, index);
//		}
//		return contextPath+servletPath+"?"+queryString;
//	}
//	public String preEdit(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		String cid = request.getParameter("cid");
//		Customer customer = customerService.load(cid);
//		request.setAttribute("cstm",customer);
//		return "f:/edit.jsp";
//	}


	public String preEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid = request.getParameter("cid");
		Customer customer = customerService.load(cid);
		request.setAttribute("cstm",customer);
		return "f:/edit.jsp";
	}

	public String edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Customer customer = CommonUtils.toBean(request.getParameterMap(), Customer.class);
		customerService.update(customer);
		request.setAttribute("msg","编辑用户成功");
		return "f:/msg.jsp";
	}

	//	public String edit(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
////		String cid = request.getParameter("cid");
//		Customer customer = CommonUtils.toBean(request.getParameterMap(), Customer.class);
////		customer.setCid(cid);
//		customerService.update(customer);
//		request.setAttribute("msg","编辑用户成功！");
//		return "f:/msg.jsp";
//	}
	public String delet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cid = request.getParameter("cid");
		customerService.delet(cid);
		request.setAttribute("msg","删除用户成功！");
		return "f:/msg.jsp";
	}
//	public String query(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		Customer criteria = CommonUtils.toBean(request.getParameterMap(), Customer.class);
//		List<Customer> cstmList = customerService.query(criteria);
//		request.setAttribute("cstmList",cstmList);
//		return "f:/list.jsp";
//	}



//
//	/**
//	 * 查询所有
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws ServletException
//	 * @throws IOException
//	 */
//	public String findAll(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		/*
//		 * 1. 调用service得到所有客户
//		 * 2. 保存到request域
//		 * 3. 转发到list.jsp
//		 */
//		request.setAttribute("cstmList", customerService.findAll());
//		return "f:/list.jsp";
//	}
//
//	/**
//	 * 编辑之前的加载工作
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws ServletException
//	 * @throws IOException
//	 */
//	public String preEdit(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		/*
//		 * 1. 获取cid
//		 * 2. 使用cid来调用service方法，得到Customer对象
//		 * 3. 把Customer保存到request域中
//		 * 4. 转发到edit.jsp显示在表单中
//		 */
//		String cid = request.getParameter("cid");
//		Customer cstm = customerService.load(cid);
//		request.setAttribute("cstm", cstm);
//		return "f:/edit.jsp";
//	}
//
//	/**
//	 * 编辑方法
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws ServletException
//	 * @throws IOException
//	 */
//	public String edit(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		/*
//		 * 1. 封装表单数据到Customer对象中
//		 * 2. 调用service方法完成修改
//		 * 3. 保存成功信息到request域
//		 * 4. 转发到msg.jsp显示成功信息
//		 */
//		// 已经封装了cid到Customer对象中
//		Customer c = CommonUtils.toBean(request.getParameterMap(), Customer.class);
//		customerService.edit(c);
//		request.setAttribute("msg", "恭喜，编辑客户成功！");
//		return "f:/msg.jsp";
//	}
//
//	public String query(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		/*
//		 * 1. 封装表单数据到Customer对象中，它只有四个属性（cname、gender、cellphone、email）
//		 *   它就是一个条件
//		 * 2. 使用Customer调用service方法，得到List<Customer>
//		 * 3. 保存到request域中
//		 * 4. 转发到list.jsp
//		 */
//		Customer criteria = CommonUtils.toBean(request.getParameterMap(), Customer.class);
//		List<Customer> cstmList = customerService.query(criteria);
//		request.setAttribute("cstmList", cstmList);
//		return "/list.jsp";
//	}

}
