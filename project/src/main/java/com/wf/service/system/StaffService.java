package com.wf.service.system;

import com.wf.entity.system.Staff;
import org.apache.shiro.authz.annotation.RequiresRoles;

import java.util.List;

public interface StaffService {
    public void add(Staff staff);
    public void delete(int id);
    @RequiresRoles({"客服部"})
    public void update(Staff staff);
    public Staff searchOne(int id);
    public List<Staff> searchAll();
    public void updateStatus(Staff staff);


}
