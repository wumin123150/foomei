package com.foomei.core.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.foomei.common.base.annotation.LogIgnore;
import com.foomei.common.dto.ErrorCodeFactory;
import com.foomei.common.dto.ResponseResult;
import com.foomei.core.entity.Annex;
import com.foomei.core.entity.User;
import com.foomei.core.service.AnnexService;
import com.foomei.core.service.UserService;
import com.foomei.core.web.CoreThreadContext;

@Api(description = "账户接口")
@RestController
@RequestMapping(value = "/api/account")
public class AccountEndpoint {
    
    @Autowired
    private UserService userService;
    @Autowired
    private AnnexService annexService;
    
    @ApiOperation(value = "保存头像", notes="图片转成base64编码", httpMethod = "POST", produces = "application/json")
    @RequestMapping(value = "saveAvatar", method = RequestMethod.POST)
    public ResponseResult saveAvatar(@ApiParam(value="base64编码的图片", required=true) String avatar) throws IOException {
        Long id = CoreThreadContext.getUserId();
        byte[] data = Base64.decodeBase64(avatar.getBytes("UTF-8"));
        Annex annex = annexService.save(data, "avatar.jpg", User.USER_ANNEX_PATH, String.valueOf(id), User.USER_ANNEX_TYPE);

        User user = userService.get(id);
        user.setAvatar(annex.getPath());
        userService.save(user);
        return ResponseResult.createSuccess(annex.getPath());
    }

    @ApiOperation(value = "密码修改", httpMethod = "POST", produces = "application/json")
    @LogIgnore(value = "field", excludes = {"password", "plainPassword"})
    @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
    public ResponseResult changePassword(@ApiParam(value="原密码", required=true) String password, @ApiParam(value="新密码", required=true) String plainPassword) {
        Long id = CoreThreadContext.getUserId();
        if (userService.checkPassword(id, password)) {
            userService.changePassword(id, plainPassword);
            return ResponseResult.SUCCEED;
        } else {
            return ResponseResult.createError(ErrorCodeFactory.BAD_REQUEST, "原密码错误");
        }
    }

}
