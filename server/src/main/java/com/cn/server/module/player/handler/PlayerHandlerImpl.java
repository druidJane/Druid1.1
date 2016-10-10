package com.cn.server.module.player.handler;

import com.cn.common.core.exception.ErrorCodeException;
import com.cn.common.core.model.Result;
import com.cn.common.core.model.ResultCode;
import com.cn.common.core.model.proto.RequestProto;
import com.cn.common.core.model.proto.ResponseProto;
import com.cn.common.core.session.Session;
import com.cn.server.module.player.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 1115 on 2016/10/9.
 */
@Component
public class PlayerHandlerImpl implements PlayerHandler {
    @Autowired
    private PlayerService playerService;
    @Override
    public Result<byte[]> login(Session session, byte[] data) {
        ResponseProto.Login result = null;
        try {
            RequestProto.Login.Builder reqLogin = RequestProto.Login.newBuilder();
            reqLogin.mergeFrom(data);
            System.out.println("用户名："+reqLogin.getUsername()+",密码："+reqLogin.getPassword());
            result = playerService.login(session, reqLogin.getUsername(), reqLogin.getPassword());
        } catch (ErrorCodeException e) {
            return Result.ERROR(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ResultCode.UNKOWN_EXCEPTION);
        }
        return Result.SUCCESS(result.toByteArray());
    }

    @Override
    public Result<byte[]> register(Session session, byte[] data) {
        ResponseProto.Register result = null;
        try {
            RequestProto.Register.Builder reqRegister = RequestProto.Register.newBuilder();
            reqRegister.mergeFrom(data);
            System.out.println("用户名："+reqRegister.getUsername()+",密码："+reqRegister.getPassword());
            result = playerService.register(session, reqRegister.getUsername(), reqRegister.getPassword());
        } catch (ErrorCodeException e) {
            return Result.ERROR(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ResultCode.UNKOWN_EXCEPTION);
        }
        return Result.SUCCESS(result.toByteArray());
    }
}
