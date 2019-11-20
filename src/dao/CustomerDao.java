package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import domain.PageBean;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import domain.Customer;
import cn.itcast.jdbc.TxQueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.xml.transform.TransformerFactory;

/**
 * 持久层
 * 
 * @author cxf
 * 
 */
public class CustomerDao {
	private QueryRunner qr = new TxQueryRunner();

	/**
	 * 添加客户
	 * 
	 * @param
	 */
	public void add(Customer c) {
		String sql="insert into t_customer values(?,?,?,?,?,?,?)";
		Object[] params={c.getCid(),c.getCname(),c.getGender(),c.getBirthday(),c.getCellphone(),c.getEmail(),c.getDescription()};
		try {
			qr.update(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
//	public void add(Customer c) {
//			try {
//				String sql = "insert into t_customer values(?,?,?,?,?,?,?)";
//				Object[] params = { c.getCid(), c.getCname(), c.getGender(),
//						c.getBirthday(), c.getCellphone(), c.getEmail(),
//						c.getDescription()};
//				qr.update(sql, params);
//			} catch(SQLException e) {
//				throw new RuntimeException(e);
//			}
//	}

//	public PageBean<Customer> findAll(int pc,int ps){
//		try {
//			PageBean<Customer> pb = new PageBean<>();
//			pb.setPc(pc);
//			pb.setPs(ps);
//			String sql="select count(*) from t_customer";
//			Number number = (Number)qr.query(sql, new ScalarHandler());
//			int tr = number.intValue();
//			pb.setTr(tr);
//			String sql2="select * from t_customer order by cname limit ?,?";
////			Object[] params={(pc-1)*ps,ps};
//			List<Customer> beanList = qr.query(sql2, new BeanListHandler<Customer>(Customer.class),(pc-1)*ps,ps);
//			pb.setBeanList(beanList);
//			return pb;
//		} catch (SQLException e) {
//			throw new RuntimeException();
//		}
//	}
	public PageBean<Customer> findAll(int pc,int ps){
		try {
		PageBean<Customer> pb = new PageBean<>();
		pb.setPc(pc);
		pb.setPs(ps);
		String sql="select count(*) from t_customer ";
		Number tr= (Number)qr.query(sql, new ScalarHandler());
		pb.setTr(tr.intValue());
		String sql1="select * from t_customer order by cname limit ?,?";
		List<Customer> beanList = qr.query(sql1, new BeanListHandler<Customer>(Customer.class), (pc - 1) * ps, ps);
		pb.setBeanList(beanList);
			return pb;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public Customer findByCid(String cid){
		String sql="select * from t_customer where cid=?";
		try {
			Customer customer = qr.query(sql, new BeanHandler<>(Customer.class),cid);
			return customer;
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}

//	public void update(Customer c) {
//		String sql="update t_customer set cname=?,gender=?,birthday=?,cellphone=?,email=?,description=? where cid=?";
//		Object[] params={c.getCname(),c.getGender(),c.getBirthday(),c.getCellphone(),c.getEmail(),c.getDescription(),c.getCid()};
//		try {
//			qr.update(sql,params);
//		} catch (SQLException e) {
//			throw new RuntimeException();
//		}
//	}
	public void update(Customer customer){
		String sql="update t_customer set cname=?,gender=?,birthday=?,cellphone=?,email=?,description=? where cid=?";
		Object[] params={customer.getCname(),customer.getGender(),customer.getBirthday(),customer.getCellphone(),
		customer.getEmail(),customer.getDescription(),customer.getCid()};
		try {
			qr.update(sql,params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void delet(String cid) {
		String sql="delete from t_customer where cid=?";
		try {
			qr.update(sql,cid);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	public PageBean<Customer> query(Customer c, int pc, int ps) throws SQLException {
		/***********拼凑where子句*************/

		/*
		 * 1. 给出基本的sql语句
		 */
		StringBuilder where = new StringBuilder("where 1=1");
		/*
		 * 2. 创建List，用来保存参数
		 */
		List<Object> params = new ArrayList<Object>();
		/*
		 * 3. 判断c中每个字段是否存在，如果存在说明有这个条件，如果不存在就没有这个条件
		 */
		String cname = c.getCname();
		if(cname != null && !cname.trim().isEmpty()) {
			where.append(" and cname like ?");
			params.add("%" + cname + "%");
		}

		String gender = c.getGender();
		if(gender != null && !gender.trim().isEmpty()) {
			where.append(" and gender=?");
			params.add(gender);
		}

		String cellphone = c.getCellphone();
		if(cellphone != null && !cellphone.trim().isEmpty()) {
			where.append(" and cellphone like ?");
			params.add("%" + cellphone + "%");
		}

		String email = c.getEmail();
		if(email != null && !email.trim().isEmpty()) {
			where.append(" and email like ?");
			params.add("%" + email + "%");
		}


		/*
		 * 得到count的sql语句
		 */
		StringBuilder countSql = new StringBuilder("select count(*) from t_customer");
		String sql = countSql.append(" ").append(where).toString();
		// 得到总记录数
		int tr = ((Number)qr.query(sql, new ScalarHandler(), params.toArray())).intValue();


		/*
		 * 查询当前页的记录
		 * 头 + where + limit
		 */
		StringBuilder selectSql = new StringBuilder("select * from t_customer");
		StringBuilder limitSql = new StringBuilder("limit ?,?");

		sql = selectSql.append(" ").append(where).append(" ").append(limitSql).toString();
		// 因为limit中存在，两个?，那么就要向params中添加两个值
		params.add((pc-1) * ps);
		params.add(ps);

		// 得到当前页记录
		List<Customer> customerList = qr.query(sql,
				new BeanListHandler<Customer>(Customer.class), params.toArray());

		/******************************/
		/*
		 * 创建PageBean
		 */
		PageBean<Customer> pb = new PageBean<Customer>();
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		pb.setBeanList(customerList);
		return pb;
	}



//	public PageBean<Customer> query(Customer criteria,int pc,int ps) {
//		StringBuilder sql=new StringBuilder("select * from t_customer where 1=1");
//		List<Object> params = new ArrayList<Object>();
//		String cname = criteria.getCname();
//		if (cname!=null&&!cname.trim().isEmpty()){
//			sql.append(" and cname like ?");
//			params.add("%"+cname+"%");
//		}
//		String gender = criteria.getGender();
//		if (gender!=null&&!gender.trim().isEmpty()){
//			sql.append(" and gender like ?");
//			params.add("%"+gender+"%");
//		}
//		String cellphone= criteria.getCellphone();
//		if (cellphone!=null&&!cellphone.trim().isEmpty()){
//			sql.append(" and cellphone like ?");
//			params.add("%"+cellphone+"%");
//		}
//		String email = criteria.getEmail();
//		if (email!=null&&!email.trim().isEmpty()){
//			sql.append(" and email like ?");
//			params.add("%"+email+"%");
//		}
//		try {
//			return qr.query(sql.toString(),new BeanListHandler<>(Customer.class),params.toArray());
//		} catch (SQLException e) {
//			throw new RuntimeException();
//		}
//	}


//	/**
//	 * 查询所有
//	 * @return
//	 */
//	public List<Customer> findAll() {
//		try {
//			String sql = "select * from t_customer";
//			return qr.query(sql, new BeanListHandler<Customer>(Customer.class));
//		} catch(SQLException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	/**
//	 * 加载客户
//	 * @param cid
//	 * @return
//	 */
//	public Customer load(String cid) {
//		try {
//			String sql = "select * from t_customer where cid=?";
//			return qr.query(sql, new BeanHandler<Customer>(Customer.class), cid);
//		} catch(SQLException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	/**
//	 * 编辑客户
//	 * @param c
//	 */
//	public void edit(Customer c) {
//		try {
//			String sql = "update t_customer set cname=?,gender=?,birthday=?," +
//					"cellphone=?,email=?,description=? where cid=?";
//			Object[] params = {c.getCname(), c.getGender(),
//					c.getBirthday(), c.getCellphone(), c.getEmail(),
//					c.getDescription(), c.getCid()};
//			qr.update(sql, params);
//		} catch(SQLException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	/**
//	 * 多条件组合查询
//	 * @param criteria
//	 * @return
//	 */
//	public List<Customer> query(Customer criteria) {
//		try {
//			/*
//			 * 1. 给出sql模板
//			 * 2. 给出参数
//			 * 3. 调用query方法，使用结果集处理器：BeanListHandler
//			 */
//			/*
//			 * 一、　给出sql模板
//			 * 二、　给出参数！
//			 */
//			/*
//			 * 1. 给出一个sql语句前半部
//			 */
//			StringBuilder sql = new StringBuilder("select * from t_customer where 1=1");
//			/*
//			 * 2. 判断条件，完成向sql中追加where子句
//			 */
//			/*
//			 * 3. 创建一个ArrayList，用来装载参数值
//			 */
//			List<Object> params = new ArrayList<Object>();
//			String cname = criteria.getCname();
//			if(cname != null && !cname.trim().isEmpty()) {
//				sql.append(" and cname like ?");
//				params.add("%" + cname + "%");
//			}
//
//			String gender = criteria.getGender();
//			if(gender != null && !gender.trim().isEmpty()) {
//				sql.append(" and gender=?");
//				params.add(gender);
//			}
//
//			String cellphone = criteria.getCellphone();
//			if(cellphone != null && !cellphone.trim().isEmpty()) {
//				sql.append(" and cellphone like ?");
//				params.add("%" + cellphone + "%");
//			}
//
//			String email = criteria.getEmail();
//			if(email != null && !email.trim().isEmpty()) {
//				sql.append(" and email like ?");
//				params.add("%" + email + "%");
//			}
//
//			/*
//			 * 三、执行query
//			 */
//			return qr.query(sql.toString(),
//					new BeanListHandler<Customer>(Customer.class),
//					params.toArray());
//		} catch(SQLException e) {
//			throw new RuntimeException(e);
//		}
//	}

}
