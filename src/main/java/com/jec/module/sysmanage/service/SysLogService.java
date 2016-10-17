package com.jec.module.sysmanage.service;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;
import com.jec.base.entity.PageList;
import com.jec.module.sysmanage.dao.SysLogDao;
import com.jec.module.sysmanage.dao.SysLogViewDao;
import com.jec.module.sysmanage.dao.UserResourceDao;
import com.jec.module.sysmanage.entity.OperateLog;
import com.jec.module.sysmanage.entity.UserResource;
import com.jec.module.sysmanage.entity.view.OperateLogView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by jeremyliu on 7/24/16.
 */
@Service
public class SysLogService extends Thread{

    @Resource
    private SysLogDao sysLogDao;

    @Resource
    private SysLogViewDao sysLogViewDao;

    @Resource
    private UserResourceDao userResourceDao;

    private Queue<OperateLog> queue = new ConcurrentLinkedDeque<>();

    private Map<String, String> resourceMap = new HashMap<>();

    private final static int batch = 10;

    @PostConstruct
    public void init(){
        List<UserResource> userResources = userResourceDao.getLogReousrce();
        for(UserResource userResource: userResources)
            for(String url: userResource.getUrlList())
                if(!url.equals(""))
                    resourceMap.put(url.trim(), userResource.getId());
        this.start();
    }

    public void addLog(int userId, String resourceId, String desc) {

        OperateLog operateLog = new OperateLog();
        operateLog.setOperater(userId);
        operateLog.setResourceId(resourceId);
        operateLog.setDescription(desc);
        queue.add(operateLog);
    }

    public void addLog(String resourceId, String desc){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if(userId != null)
            addLog(userId, resourceId, desc);
    }

    public void addLog(String desc){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String uri = StringUtils.trimLeadingCharacter(request.getPathInfo(),'/');
        String resId = resourceMap.get(uri);
        if(resId!=null)
            addLog(resId, desc);
    }

    @Transactional(readOnly = true)
    public PageList<OperateLogView> fetchLog(int start, int page,
                                             Date startDate, Date endDate,
                                             String searchKey) {
        Search search = new Search(OperateLogView.class);
        search.setFirstResult(start);
        search.setMaxResults(page);

        if(startDate != null)
            search.addFilterGreaterThan("createTime", startDate);

        if(endDate != null)
            search.addFilterLessOrEqual("createTime", endDate);

        if(searchKey != null && !searchKey.equals("")){
            searchKey = "%" + searchKey + "%";
            Filter operatorFilter = new Filter("operator", searchKey, Filter.OP_ILIKE);
            Filter moduleFilter = new Filter("module", searchKey, Filter.OP_ILIKE);
            Filter descFilter = new Filter("description", searchKey, Filter.OP_ILIKE);
            search.addFilter(Filter.or(operatorFilter, moduleFilter, descFilter));
        }

        SearchResult<OperateLogView> result = sysLogViewDao.searchAndCount(search);
        int totalCount = result.getTotalCount();
        int totalPage = (totalCount+ page) / page;
        return new PageList<>(result.getResult(), totalCount, totalPage, (start+page)/page);
    }


    @Transactional
    public void batchRemoveLog(Integer[] ids){
        sysLogDao.removeByIds(ids);
    }

    @Override
    public void run(){
        TransactionSynchronizationManager.initSynchronization();
        OperateLog[] operateLogs = new OperateLog[batch];
        while(true){

            for(int i=0; i<batch; i++){
                if(queue.size() == 0) {
                    if(i > 0)
                        sysLogDao.saveForce(Arrays.copyOfRange(operateLogs, 0, i));
                    break;
                }else {
                    OperateLog operateLog = queue.poll();
                    operateLogs[i] = operateLog;
                }
            }
            try {
                Thread.sleep(50);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}

