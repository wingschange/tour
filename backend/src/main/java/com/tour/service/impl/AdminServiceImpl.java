package com.tour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tour.entity.*;
import com.tour.mapper.*;
import com.tour.service.AdminService;
import com.tour.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private FollowMapper followMapper;

    @Override
    public IPage<UserVo> listUsers(int page, int size) {
        IPage<User> userPage = userMapper.selectPage(new Page<>(page, size), null);
        return userPage.convert(this::buildUserVo);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userMapper.deleteById(userId);
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
    }

    @Override
    @Transactional
    public void updateUserRole(Long userId, String roleName) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getName, roleName));
        if (role == null) {
            throw new IllegalArgumentException("Role not found: " + roleName);
        }
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        UserRole userRole = UserRole.builder().userId(userId).roleId(role.getId()).build();
        userRoleMapper.insert(userRole);
    }

    @Override
    public IPage<Post> listAllPosts(int page, int size) {
        return postMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<Post>().orderByDesc(Post::getCreateTime));
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        postMapper.deleteById(postId);
    }

    @Override
    public IPage<Section> listSections(int page, int size) {
        return sectionMapper.selectPage(new Page<>(page, size), null);
    }

    private UserVo buildUserVo(User user) {
        UserVo vo = new UserVo();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setEmail(user.getEmail());
        vo.setAvatar(user.getAvatar());
        vo.setBio(user.getBio());
        vo.setCreateTime(user.getCreateTime());

        List<UserRole> userRoles = userRoleMapper.selectList(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getId()));
        if (!userRoles.isEmpty()) {
            List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            List<Role> roles = roleMapper.selectBatchIds(roleIds);
            vo.setRoles(roles.stream().map(Role::getName).collect(Collectors.toList()));
        } else {
            vo.setRoles(new ArrayList<>());
        }

        long followerCount = followMapper.selectCount(
                new LambdaQueryWrapper<Follow>().eq(Follow::getFolloweeId, user.getId()));
        long followingCount = followMapper.selectCount(
                new LambdaQueryWrapper<Follow>().eq(Follow::getFollowerId, user.getId()));
        vo.setFollowerCount((int) followerCount);
        vo.setFollowingCount((int) followingCount);

        return vo;
    }
}
