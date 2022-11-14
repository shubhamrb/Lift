package com.liftPlzz.api;


import com.google.gson.JsonObject;
import com.liftPlzz.model.BlockUserResponse;
import com.liftPlzz.model.FindLiftResponse;
import com.liftPlzz.model.ResponseChatSuggestion;
import com.liftPlzz.model.UserInfo.UserInfoModel;
import com.liftPlzz.model.chatuser.ResponseChatUser;
import com.liftPlzz.model.completedLift.ResponseCompletedLift;
import com.liftPlzz.model.createLift.CreateLiftResponse;
import com.liftPlzz.model.createProfile.CreateProfileMainResponse;
import com.liftPlzz.model.createTicket.ResponseCreateTicket;
import com.liftPlzz.model.createVehicle.CreateVehicleResponse;
import com.liftPlzz.model.deleteVehicle.DeleteVehicleMainResponse;
import com.liftPlzz.model.editlift.GetVehicleEditResponse;
import com.liftPlzz.model.findVehicle.FindVehicleResponse;
import com.liftPlzz.model.getFaq.ResponseFaq;
import com.liftPlzz.model.getNotification.ResponseNotification;
import com.liftPlzz.model.getTicketCategory.ResponseTicketCategory;
import com.liftPlzz.model.getTicketDetails.ResponseTicketDetails;
import com.liftPlzz.model.getTicketList.ResponseTicketList;
import com.liftPlzz.model.getVehicle.GetVehicleListMainResponse;
import com.liftPlzz.model.getVehicle.getReview.GetReviewMainResponse;
import com.liftPlzz.model.getsetting.SettingModel;
import com.liftPlzz.model.matchingridemodel.MatchingRideByCategoryResponse;
import com.liftPlzz.model.on_going.MainOnGoingResponse;
import com.liftPlzz.model.partnerdetails.Example;
import com.liftPlzz.model.pointsRedemption.PointsModel;
import com.liftPlzz.model.recharge.RechargeFuelCardHistoryResponse;
import com.liftPlzz.model.recharge.RechargeHistoryResponse;
import com.liftPlzz.model.resendOtp.ResendOtpResponse;
import com.liftPlzz.model.ridebyvehicletypemodel.DriverByTypeReponse;
import com.liftPlzz.model.ridehistorymodel.RideHistoryResponse;
import com.liftPlzz.model.riderequestmodel.RideRequestResponse;
import com.liftPlzz.model.sendotp.MainResponse;
import com.liftPlzz.model.upcomingLift.UpcomingLiftResponse;
import com.liftPlzz.model.vehiclesubcategory.VehicleSubCategoryModel;
import com.liftPlzz.model.verifyOtp.VerifyOtpMainResponse;
import com.liftPlzz.model.videos.upcomingLift.VideosResponse;
import com.liftPlzz.model.viewRideDetails.ViewRideDetailsResponse;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiService {

    @FormUrlEncoded
    @POST("send-otp")
    Call<MainResponse> sendOTP(@Field("api_key") String api_key,
                               @Field("client") String client,
                               @Field("mobile_no") String mobile_no);

    @FormUrlEncoded
    @POST("verify-otp")
    Call<VerifyOtpMainResponse> verifyOTP(@Field("api_key") String api_key,
                                          @Field("client") String client,
                                          @Field("otp") String otp,
                                          @Field("mobile_no") String mobile_no);

    @FormUrlEncoded
    @POST("resend-otp")
    Call<ResendOtpResponse> resendOTP(@Field("api_key") String api_key,
                                      @Field("client") String client,
                                      @Field("mobile_no") String mobile_no);

    @FormUrlEncoded
    @POST("get-vehicle-list")
    Call<GetVehicleListMainResponse> get_vehicle_list(@Field("api_key") String api_key,
                                                      @Field("client") String client,
                                                      @Field("token") String token,
                                                      @Field("total_km") String total_km);

    @FormUrlEncoded
    @POST("edit-lift")
    Call<GetVehicleEditResponse> get_lift_detail(@Field("api_key") String api_key,
                                                 @Field("client") String client,
                                                 @Field("lift_id") String lift_id,
                                                 @Field("token") String token);


    @FormUrlEncoded
    @POST("get-driver-list")
    Call<GetVehicleListMainResponse> getDriverList(@Field("api_key") String api_key,
                                                   @Field("client") String client,
                                                   @Field("token") String token);

    //rc_image vehicle_image_back vehicle_image_front
    @Multipart
    @POST("create-vehicle")
    Call<CreateVehicleResponse> create_vehicle(@Part("api_key") RequestBody api_key,
                                               @Part("client") RequestBody client,
                                               @Part("token") RequestBody token,
                                               @Part("type") RequestBody type,
                                               @Part("model") RequestBody model,
                                               @Part("registration_no") RequestBody registration_no,
                                               @Part("insurance_date") RequestBody insurance_date,
                                               @Part("rate_per_km") RequestBody ratePerkm,
                                               @Part("seats") RequestBody seats,
                                               @Part("is_default") RequestBody is_default,
                                               @Part("vehicle_subcategory") RequestBody vehicleSubcategory,
                                               @Part MultipartBody.Part vehicle_image_front,
                                               @Part MultipartBody.Part vehicle_image_back,
                                               @Part MultipartBody.Part rc_image);

    @Multipart
    @POST("update-vehicle")
    Call<CreateVehicleResponse> updateVehicle(@Part("api_key") RequestBody api_key,
                                              @Part("client") RequestBody client,
                                              @Part("token") RequestBody token,
                                              @Part("vehicle_id") RequestBody vehicle_id,
                                              @Part("vehicle_type") RequestBody type,
                                              @Part("vehicle_model") RequestBody model,
                                              @Part("registration_no") RequestBody registration_no,
                                              @Part("insurance_date") RequestBody insurance_date,
                                              @Part("rate_per_km") RequestBody ratePerKm,
                                              @Part("seats") RequestBody seats,
                                              @Part("is_default") RequestBody is_default,
                                              @Part("vehicle_subcategory") RequestBody vehicleSubcategory,
                                              @Part MultipartBody.Part vehicle_image_front,
                                              @Part MultipartBody.Part vehicle_image_back,
                                              @Part MultipartBody.Part rc_image);

    //image
    @Multipart
    @POST("edit-profile")
    Call<CreateProfileMainResponse> create_profile_image(@Part("api_key") RequestBody api_key,
                                                         @Part("client") RequestBody client,
                                                         @Part("token") RequestBody token,
                                                         @Part("name") RequestBody name,
                                                         @Part("email") RequestBody email,
                                                         @Part("mobile") RequestBody mobile,
                                                         @Part("designation") RequestBody designation,
                                                         @Part("about_me") RequestBody about_me,
                                                         @Part MultipartBody.Part imageFile);

    @FormUrlEncoded
    @POST("edit-profile")
    Call<CreateProfileMainResponse> create_profile(@Field("api_key") String api_key,
                                                   @Field("client") String client,
                                                   @Field("token") String token,
                                                   @Field("name") String name,
                                                   @Field("email") String email,
                                                   @Field("mobile") String mobile,
                                                   @Field("designation") String designation,
                                                   @Field("about_me") String about_me);

    @FormUrlEncoded
    @POST("get-user-review")
    Call<GetReviewMainResponse> get_review(@Field("api_key") String api_key,
                                           @Field("client") String client,
                                           @Field("token") String token,
                                           @Field("user_id") String user_id,
                                           @Field("filter_type") String filter_type);

    @FormUrlEncoded
    @POST("review-like")
    Call<GetReviewMainResponse> like_review(@Field("api_key") String api_key,
                                            @Field("client") String client,
                                            @Field("token") String token,
                                            @Field("review_id") int review_id);

    @FormUrlEncoded
    @POST("review-dislike")
    Call<GetReviewMainResponse> dislike_review(@Field("api_key") String api_key,
                                               @Field("client") String client,
                                               @Field("token") String token,
                                               @Field("review_id") int review_id);

    @FormUrlEncoded
    @POST("remove-social-image")
    Call<GetReviewMainResponse> delete_eimag(@Field("api_key") String api_key,
                                             @Field("client") String client,
                                             @Field("token") String token,
                                             @Field("id") String id);

    // ------------jagnarayan-----------//
    //  https://charpair.com/api/chat-user-list
    @FormUrlEncoded
    @POST("chat-user-list")
    Call<ResponseChatUser> getChatUser(@Field("api_key") String api_key,
                                       @Field("client") String client,
                                       @Field("token") String token);

    //  ------------ end jagnarayan----------- //

    @FormUrlEncoded
    @POST("update-push-notification")
    Call<FindLiftResponse> update_push_notification(@Field("api_key") String api_key,
                                                    @Field("client") String client,
                                                    @Field("token") String token,
                                                    @Field("notification_id") String notification_id);


    @FormUrlEncoded
    @POST("edit-profile")
    Call<CreateProfileMainResponse> edit_profile(@Field("api_key") String api_key,
                                                 @Field("client") String client,
                                                 @Field("token") String token,
                                                 @Field("name") String name,
                                                 @Field("dob") String dob,
                                                 @Field("email") String email,
                                                 @Field("gender") String gender,
                                                 @Field("mobile") String mobile,
                                                 @Field("designation") String designation,
                                                 @Field("department") String department,
                                                 @Field("company") String company,
                                                 @Field("about_me") String about_me,
                                                 @Field("sos") String sos,
                                                 @Field("is_email_public") int is_email_public,
                                                 @Field("is_dob_public") int is_dob_public,
                                                 @Field("is_contact_public") int is_contact_public);

    @FormUrlEncoded
    @POST("find-lift")
    Call<FindLiftResponse> find_lift(@Field("api_key") String api_key,
                                     @Field("client") String client,
                                     @Field("token") String token,
                                     @Field("title") String title,
                                     @Field("requir_seats") String requir_seats,
                                     @Field("start_point") String start_point,
                                     @Field("end_point") String end_point,
                                     @Field("lift_date") String lift_date,
                                     @Field("lift_time") String liftTime,
                                     @Field("total_km") String total_km);

    @FormUrlEncoded
    @POST("update-lift")
    Call<FindLiftResponse> findUpdate_lift(@Field("api_key") String api_key,
                                           @Field("client") String client,
                                           @Field("token") String token,
                                           @Field("title") String title,
                                           @Field("paid_seats") String requir_seats,
                                           @Field("start_point") String start_point,
                                           @Field("end_point") String end_point,
                                           @Field("lift_date") String lift_date,
                                           @Field("lift_id") String lift_id,
                                           @Field("lift_time") String liftTime,
                                           @Field("total_km") String total_km);

    // FIND UPDATE  paid_seats,  , lift_id


    //checkpoints

    @FormUrlEncoded
    @POST("update-lift")
    Call<CreateLiftResponse> offerUpdate_lift(@Field("api_key") String api_key,
                                              @Field("client") String client,
                                              @Field("token") String token,
                                              @Field("vehicle_id") String vehicle_id,
                                              @Field("lift_type") String lift_type,
                                              @Field("free_seats") String free_seats,
                                              @Field("paid_seats") String paid_seats,
                                              @Field("start_point") String start_point,
                                              @Field("end_point") String end_point,
                                              @Field("checkpoints") String checkpoints,
                                              @Field("lift_date") String lift_date,
                                              @Field("lift_id") String lift_id,
                                              @Field("lift_time") String liftTime,
                                              @Field("total_km") String total_km, @Field("rate_per_km") String rate_per_km);

    @FormUrlEncoded
    @POST("create-lift")
    Call<CreateLiftResponse> create_lift(@Field("api_key") String api_key,
                                         @Field("client") String client,
                                         @Field("token") String token,
                                         @Field("vehicle_id") String vehicle_id,
                                         @Field("lift_type") String lift_type,
                                         @Field("free_seats") String free_seats,
                                         @Field("paid_seats") String paid_seats,
                                         @Field("start_point") String start_point,
                                         @Field("end_point") String end_point,
                                         @Field("checkpoints") String checkpoints,
                                         @Field("lift_date") String lift_date,
                                         @Field("lift_time") String liftTime,
                                         @Field("total_km") String total_km,
                                         @Field("rate_per_km") String rate_per_km);

//     ,


    @FormUrlEncoded
    @POST("get-profile")
    Call<CreateProfileMainResponse> get_profile(@Field("api_key") String api_key,
                                                @Field("client") String client,
                                                @Field("token") String token);

    @FormUrlEncoded
    @POST("setting-reset")
    Call<JsonObject> reset_setting(@Field("api_key") String api_key,
                                   @Field("client") String client,
                                   @Field("token") String token);

    @FormUrlEncoded
    @POST("my-upcomming-lifts")
    Call<UpcomingLiftResponse> my_upcoming_lifts(@Field("api_key") String api_key,
                                                 @Field("client") String client,
                                                 @Field("token") String token);

    @FormUrlEncoded
    @POST("delete-vehicle")
    Call<DeleteVehicleMainResponse> delete_vehicle(@Field("api_key") String api_key,
                                                   @Field("client") String client,
                                                   @Field("token") String token,
                                                   @Field("vehicle_id") String vehicle_id
    );

    @Multipart
    @POST("add-social-image")
    Call<CreateProfileMainResponse> add_social_image(@Part("api_key") RequestBody api_key,
                                                     @Part("client") RequestBody client,
                                                     @Part("token") RequestBody token,
                                                     @Part MultipartBody.Part imageFile);

    @Multipart
    @POST("add-face-image")
    Call<CreateProfileMainResponse> add_selfie(@Part("api_key") RequestBody api_key,
                                               @Part("client") RequestBody client,
                                               @Part("token") RequestBody token,
                                               @Part MultipartBody.Part imageFile);

    @Multipart
    @POST("add-govtid-image")
    Call<CreateProfileMainResponse> add_Id(@Part("api_key") RequestBody api_key,
                                           @Part("client") RequestBody client,
                                           @Part("token") RequestBody token,
                                           @Part MultipartBody.Part imageFile);


    @FormUrlEncoded
    @POST("find-vehicle")
    Call<FindVehicleResponse> find_vehicle(@Field("api_key") String api_key,
                                           @Field("client") String client,
                                           @Field("token") String token,
                                           @Field("lift_id") String lift_id
    );

    @FormUrlEncoded
    @POST("view-ride-details")
    Call<ViewRideDetailsResponse> view_ride_details(@Field("api_key") String api_key,
                                                    @Field("client") String client,
                                                    @Field("token") String token,
                                                    @Field("lift_id") String lift_id
    );

    @FormUrlEncoded
    @POST("fetch-all-notifications")
    Call<ViewRideDetailsResponse> getAllNotification(@Field("api_key") String api_key,
                                                     @Field("client") String client,
                                                     @Field("token") String token);

    @FormUrlEncoded
    @POST("Get-Vehicle-Subcategory")
    Call<VehicleSubCategoryModel> getVehicleSubCategory(@Field("api_key") String api_key,
                                                        @Field("client") String client,
                                                        @Field("token") String token,
                                                        @Field("type") String type);

    @FormUrlEncoded
    @POST("matching-rides-by-category")
    Call<MatchingRideByCategoryResponse> getMatchingRideByCategory(@Field("api_key") String api_key,
                                                                   @Field("client") String client,
                                                                   @Field("token") String token,
                                                                   @Field("lift_id") int liftId);

    @FormUrlEncoded
    @POST("search-history")
    Call<RideHistoryResponse> getRideHistory(@Field("api_key") String api_key,
                                             @Field("client") String client,
                                             @Field("token") String token);

    @FormUrlEncoded
    @POST("matching-rides-by-vehicle-type")
    Call<DriverByTypeReponse> getRideByVehicleType(@Field("api_key") String api_key,
                                                   @Field("client") String client,
                                                   @Field("token") String token,
                                                   @Field("vehicle_subcategory_id") int subCategoryId,
                                                   @Field("lift_id") int liftId,
                                                   @Field("vehicle_type") String vehicleType);

    @FormUrlEncoded
    @POST("matching-rides-for-driver")
    Call<DriverByTypeReponse> getRideByDriver(@Field("api_key") String api_key,
                                              @Field("client") String client,
                                              @Field("token") String token,
                                              @Field("vehicle_subcategory_id") int subCategoryId,
                                              @Field("lift_id") int liftId);

    @FormUrlEncoded
    @POST("send-Invitation-For-Lift")
    Call<ResponseBody> sendInvitation(@Field("api_key") String api_key,
                                      @Field("client") String client,
                                      @Field("token") String token,
                                      @Field("lift_id") int liftId,
                                      @Field("from_lift_id") int fromLiftId);


    @FormUrlEncoded
    @POST("Lift-Invite-List")
    Call<RideRequestResponse> getRideRequest(@Field("api_key") String api_key,
                                             @Field("client") String client,
                                             @Field("token") String token,
                                             @Field("lift_id") int liftId);

    @FormUrlEncoded
    @POST("Lift-Invitation-Status-Update")
    Call<ResponseBody> updateInvitationStatus(@Field("api_key") String api_key,
                                              @Field("client") String client,
                                              @Field("token") String token,
                                              @Field("invite_id") int inviteId,
                                              @Field("status") int status);

    @FormUrlEncoded
    @POST("partner-remove")
    Call<ResponseBody> cancelPartnerRide(@Field("api_key") String api_key,
                                         @Field("client") String client,
                                         @Field("token") String token,
                                         @Field("request_id") int requestId,
                                         @Field("my_lift_id") int liftId,
                                         @Field("reason") String reason
    );

    @FormUrlEncoded
    @POST("user-detail")
    Call<UserInfoModel> getUserDetails(@Field("api_key") String api_key,
                                       @Field("client") String client,
                                       @Field("user_id") int userId);


    @FormUrlEncoded
    @POST("driver-detail")
    Call<UserInfoModel> getDriverDetails(@Field("api_key") String api_key,
                                         @Field("client") String client,
                                         @Field("token") String token,
                                         @Field("lift_id") int lift_ID);

    @FormUrlEncoded
    @POST("my-ongoing-ride")
    Call<MainOnGoingResponse> rideOnGoing(@Field("api_key") String api_key,
                                          @Field("client") String client,
                                          @Field("token") String token);


    @FormUrlEncoded
    @POST("cancel-invitation-for-lift")
    Call<ResponseBody> cancelInvitation(@Field("api_key") String api_key,
                                        @Field("client") String client,
                                        @Field("token") String token,
                                        @Field("lift_id") int liftId,
                                        @Field("reason") String reason);

    @FormUrlEncoded
    @POST("partner-detail")
    Call<Example> getPartnerDetails(@Field("api_key") String api_key,
                                    @Field("client") String client,
                                    @Field("token") String token,
                                    @Field("lift_id") int liftId);

    @FormUrlEncoded
    @POST("get-settings-list")
    Call<SettingModel> getSettingList(@Field("api_key") String api_key,
                                      @Field("client") String client,
                                      @Field("token") String token);

    @FormUrlEncoded
    @POST("update-user-setting")
    Call<SettingModel> updateUserSetting(@Field("api_key") String api_key,
                                         @Field("client") String client,
                                         @Field("token") String token,
                                         @Field("setting_id") int settingId,
                                         @Field("input_value") int inputValue);

    @FormUrlEncoded
    @POST("update-user-setting")
    Call<SettingModel> updateUserSetting(@Field("api_key") String api_key,
                                         @Field("client") String client,
                                         @Field("token") String token,
                                         @Field("setting_id") int settingId,
                                         @Field("input_value") String inputValue);

    @FormUrlEncoded
    @POST("update-user-setting")
    Call<SettingModel> updateUserSetting(@Field("api_key") String api_key,
                                         @Field("client") String client,
                                         @Field("token") String token,
                                         @Field("setting_id") int settingId,
                                         @Field("input_value") JSONObject inputValue);

    @FormUrlEncoded
    @POST("create-ticket")
    Call<ResponseCreateTicket> createTicket(@Field("email") String email,
                                            @Field("subject") String subject,
                                            @Field("category") String category,
                                            @Field("description") String description,
                                            @Field("api_key") String api_key,
                                            @Field("client") String client,
                                            @Field("token") String token);

    @FormUrlEncoded
    @POST("ticket-list")
    Call<ResponseTicketList> getTicketList(@Field("api_key") String api_key,
                                           @Field("client") String client,
                                           @Field("token") String token);


    @FormUrlEncoded
    @POST("ticket-details")
    Call<ResponseTicketDetails> getTicketDetails(@Field("api_key") String api_key,
                                                 @Field("client") String client,
                                                 @Field("token") String token,
                                                 @Field("ticket_id") String ticket_id);

    @GET("get-ticket-category")
    Call<ResponseTicketCategory> getTicketCategory();


    @FormUrlEncoded
    @POST("user-notification-list")
    Call<ResponseNotification> getNotificationList(@Field("api_key") String api_key,
                                                   @Field("client") String client,
                                                   @Field("token") String token);

    @FormUrlEncoded
    @POST("block-unblock-user-list")
    Call<BlockUserResponse> getBlockedUsers(@Field("api_key") String api_key,
                                            @Field("client") String client,
                                            @Field("token") String token);

    @FormUrlEncoded
    @POST("block-unblock-user")
    Call<JsonObject> blockUser(@Field("api_key") String api_key,
                               @Field("client") String client,
                               @Field("token") String token,
                               @Field("user_id") int user_id,
                               @Field("reason") String reason
    );

    @FormUrlEncoded
    @POST("my-completed-lifts")
    Call<ResponseCompletedLift> getCompletedLift(@Field("api_key") String api_key,
                                                 @Field("client") String client,
                                                 @Field("token") String token);

    @FormUrlEncoded
    @POST("faq-questions")
    Call<ResponseFaq> getFAQ(@Field("api_key") String api_key,
                             @Field("client") String client);

    @FormUrlEncoded
    @POST("get-invoice")
    Call<ResponseFaq> getInvoice(@Field("api_key") String api_key,
                                 @Field("client") String client,
                                 @Field("token") String token,
                                 @Field("request_id") int requestId,
                                 @Field("user_id") int userId);


    @GET("chat-suggestions")
    Call<ResponseChatSuggestion> getChatSuggestions();


    @FormUrlEncoded
    @POST("set-reviews")
    Call<ResponseBody> setReview(@Field("api_key") String api_key,
                                 @Field("client") String client,
                                 @Field("token") String token,
                                 @Field("to_user") int requestId,
                                 @Field("rating") int userId,
                                 @Field("feedback") String feedBack);

    @FormUrlEncoded
    @POST("cancel-my-lift")
    Call<ResponseBody> cancelMyLift(@Field("api_key") String api_key,
                                    @Field("client") String client,
                                    @Field("token") String token,
                                    @Field("lift_id") int liftId);

    @FormUrlEncoded
    @POST("lift-delete")
    Call<ResponseBody> deleteMyLift(@Field("api_key") String api_key,
                                    @Field("client") String client,
                                    @Field("token") String token,
                                    @Field("lift_id") int liftId);

    @FormUrlEncoded
    @POST("ride-end")
    Call<JsonObject> rideEnd(@Field("api_key") String api_key,
                             @Field("client") String client,
                             @Field("token") String token,
                             @Field("request_id") int requestId,
                             @Field("ride_end_latlong") String lat_long,
                             @Field("city") String city,
                             @Field("address") String address
    );

    @FormUrlEncoded
    @POST("ride-end-accept-by-driver")
    Call<JsonObject> rideEndRequestAccept(@Field("api_key") String api_key,
                                          @Field("client") String client,
                                          @Field("token") String token,
                                          @Field("request_id") int requestId);

    @FormUrlEncoded
    @POST("driver-liftend")
    Call<JsonObject> ridebyDriverEnd(@Field("api_key") String api_key,
                                     @Field("client") String client,
                                     @Field("token") String token,
                                     @Field("lift_id") int requestId,
                                     @Field("lat") double lat,
                                     @Field("long") double logitute);


    @FormUrlEncoded
    @POST("liftstart-codematch")
    Call<JsonObject> liftStartCodeMatch(@Field("api_key") String api_key,
                                        @Field("client") String client,
                                        @Field("token") String token,
                                        @Field("lift_id") int requestId,
                                        @Field("code") int code,
                                        @Field("ride_start_latlong") String lat_long,
                                        @Field("city") String city,
                                        @Field("address") String address
    );

    @FormUrlEncoded
    @POST("card-recharge")
    Call<PointsModel> rechargeFuelCard(@Field("api_key") String api_key,
                                       @Field("client") String client,
                                       @Field("token") String token,
                                       @Field("point") String points);

    @FormUrlEncoded
    @POST("card-apply")
    Call<PointsModel> requestNewCard(@Field("api_key") String api_key,
                                     @Field("client") String client,
                                     @Field("token") String token,
                                     @Field("full_name") String full_name,
                                     @Field("address") String address,
                                     @Field("point") String points);

    @FormUrlEncoded
    @POST("card-detail")
    Call<PointsModel> cardDetail(@Field("api_key") String api_key,
                                 @Field("client") String client,
                                 @Field("token") String token);

    @FormUrlEncoded
    @POST("wallet-recharge-history")
    Call<RechargeHistoryResponse> rechargeWalletHistory(@Field("api_key") String api_key,
                                                        @Field("client") String client,
                                                        @Field("token") String token);

    @FormUrlEncoded
    @POST("fuelcard-recharge-history")
    Call<RechargeFuelCardHistoryResponse> rechargeFuelCardHistory(@Field("api_key") String api_key,
                                                                  @Field("client") String client,
                                                                  @Field("token") String token);


    @GET("how_to_use_videos")
    Call<VideosResponse> getVideos();

    @FormUrlEncoded
    @POST("feedback")
    Call<BlockUserResponse> submitFeedback(@Field("api_key") String api_key,
                                           @Field("client") String client,
                                           @Field("token") String token,
                                           @Field("message") String message);

    @FormUrlEncoded
    @POST("suggestion")
    Call<BlockUserResponse> submitSuggestion(@Field("api_key") String api_key,
                                             @Field("client") String client,
                                             @Field("token") String token,
                                             @Field("message") String message);

//    api_key:070b92d28adc166b3a6c63c2d44535d2f62a3e24
//    client:android
//    token:NRy4MvEaDj5O04r8S6GGSZAJ7T5tv1QvS969rtgyYe7qdyKv8q6wjWBozH5I
//    request_id:57
//    code:0100
//    lat:91.5656252
//    long:75.0002356

    //X14ybrF0H7tjrwG5w0DMZIs2Two5EiYx73LGmLMF0nt39zLVfEne1NuOhZF9

}
