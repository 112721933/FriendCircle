package razerdp.friendcircle.app.net.request;

import android.text.TextUtils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import razerdp.friendcircle.app.net.base.BaseRequestClient;
import razerdp.friendcircle.mvp.model.entity.CommentInfo;

/**
 * Created by 大灯泡 on 2016/12/13.
 * <p>
 * 删除评论
 */

public class DeleteCommentRequest extends BaseRequestClient<String> {

    private String commentid;

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }


    @Override
    protected void executeInternal(final int requestType, boolean showDialog) {
        if (TextUtils.isEmpty(commentid)) {
            onResponseError(new BmobException("评论ID为空"), getRequestType());
            return;
        }
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setObjectId(commentid);
        commentInfo.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    onResponseSuccess(commentid, requestType);
                } else {
                    onResponseError(e, requestType);
                }
            }
        });
    }
}
