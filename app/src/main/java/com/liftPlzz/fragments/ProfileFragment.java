package com.liftPlzz.fragments

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.with
import com.liftPlzz.base.BaseFragment
import com.liftPlzz.presenter.ProfilePresenter
import com.liftPlzz.views.ProfileView
import com.liftPlzz.adapter.ReviewListAdapter
import butterknife.BindView
import androidx.viewpager.widget.ViewPager
import me.relex.circleindicator.CircleIndicator
import butterknife.OnClick
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.liftPlzz.R
import com.liftPlzz.adapter.ViewPagerAdapter
import com.liftPlzz.fragments.UpdateProfileFragment
import com.liftPlzz.model.SocialImage
import com.liftPlzz.model.createProfile.User
import com.liftPlzz.model.getVehicle.getReview.Datum
import com.liftPlzz.utils.Constants
import okhttp3.RequestBody
import okhttp3.MultipartBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import java.util.ArrayList
import java.util.HashMap

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : BaseFragment<ProfilePresenter?, ProfileView?>(), ProfileView,
    ReviewListAdapter.ItemListener, ViewPagerAdapter.ItemListener {
    var sharedPreferences: SharedPreferences? = null

    @JvmField
    @BindView(R.id.imageViewBackContact)
    var imageViewBackContact: ImageView? = null

    @JvmField
    @BindView(R.id.layoutAbout)
    var layoutAbout: LinearLayout? = null

    @JvmField
    @BindView(R.id.textViewAbout)
    var textViewAbout: AppCompatTextView? = null

    @JvmField
    @BindView(R.id.layoutAddPoint)
    var layoutAddPoint: LinearLayout? = null

    @JvmField
    @BindView(R.id.AddPointtextview)
    var AddPointtextview: AppCompatTextView? = null

    @JvmField
    @BindView(R.id.layoutReview)
    var layoutReview: LinearLayout? = null

    @JvmField
    @BindView(R.id.imgback)
    var imgback: ImageView? = null

    @JvmField
    @BindView(R.id.textViewReview)
    var textViewReview: AppCompatTextView? = null

    @JvmField
    @BindView(R.id.recyclerViewReview)
    var recyclerViewReview: RecyclerView? = null

    @JvmField
    @BindView(R.id.scrollViewAbout)
    var scrollViewAbout: ScrollView? = null

    @JvmField
    @BindView(R.id.textViewMobile)
    var textViewMobile: AppCompatTextView? = null

    @JvmField
    @BindView(R.id.textViewEmail)
    var textViewEmail: AppCompatTextView? = null

    @JvmField
    @BindView(R.id.textViewReviewCount)
    var textViewReviewCount: AppCompatTextView? = null

    @JvmField
    @BindView(R.id.profilePercentTxt)
    var profilePercentTxt: AppCompatTextView? = null

    @JvmField
    @BindView(R.id.textViewRating)
    var textViewRating: AppCompatTextView? = null

    @JvmField
    @BindView(R.id.textViewAboutUser)
    var textViewAboutUser: AppCompatTextView? = null

    @JvmField
    @BindView(R.id.textViewTotalLift)
    var textViewTotalLift: AppCompatTextView? = null

    @JvmField
    @BindView(R.id.textViewGiverLift)
    var textViewGiverLift: AppCompatTextView? = null

    @JvmField
    @BindView(R.id.textViewTakerLift)
    var textViewTakerLift: AppCompatTextView? = null

    @JvmField
    @BindView(R.id.editTextName)
    var editTextName: AppCompatTextView? = null

    @JvmField
    @BindView(R.id.tv_shareCode)
    var tvShareCode: AppCompatTextView? = null

    @JvmField
    @BindView(R.id.imageViewEdit)
    var imageViewEdit: AppCompatImageView? = null
    var userData: User? = null

    @JvmField
    @BindView(R.id.viewPagerMain)
    var viewPagerMain: ViewPager? = null
    var mViewPagerAdapter: ViewPagerAdapter? = null

    @JvmField
    @BindView(R.id.indicator)
    var indicator: CircleIndicator? = null
    var imageslist: MutableList<SocialImage>? = null
    private var strToken: String? = null
    override fun createLayout(): Int {
        return R.layout.fragment_profile
    }

    override fun setPresenter() {
        presenter = ProfilePresenter()
    }

    override fun createView(): ProfileView {
        return this
    }

    override fun bindData() {
        sharedPreferences =
            requireActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        strToken = sharedPreferences?.getString(Constants.TOKEN, "")
        presenter!!.getProfile(sharedPreferences?.getString(Constants.TOKEN, ""))
        presenter!!.getReview(sharedPreferences?.getString(Constants.TOKEN, ""))
        layoutAbout!!.isSelected = true
        textViewAbout!!.isSelected = true
        layoutReview!!.isSelected = false
        textViewReview!!.isSelected = false
        scrollViewAbout!!.visibility = View.VISIBLE
        recyclerViewReview!!.visibility = View.GONE
    }

    @OnClick(
        R.id.imageViewBackContact,
        R.id.imgback,
        R.id.layoutAbout,
        R.id.layoutReview,
        R.id.imageViewEdit,
        R.id.layoutAddPoint,
        R.id.textViewReviewCount
    )
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.imageViewBackContact -> requireActivity().onBackPressed()
            R.id.imageViewEdit -> {
                if (userData != null) {
                    UpdateProfileFragment.setUser(userData)
                }
                presenter!!.openUpdateProfile()
            }
            R.id.imgback -> {
                layoutAbout!!.isSelected = true
                textViewAbout!!.isSelected = true
                layoutReview!!.isSelected = false
                textViewReview!!.isSelected = false
                layoutAddPoint!!.isSelected = false
                scrollViewAbout!!.visibility = View.VISIBLE
                recyclerViewReview!!.visibility = View.GONE
                imgback!!.visibility = View.GONE
            }
            R.id.layoutAddPoint -> {
                layoutAddPoint!!.isSelected = true
                AddPointtextview!!.isSelected = true
                layoutAbout!!.isSelected = false
                layoutReview!!.isSelected = false
                Log.d("tok", strToken!!)
                getpoints()
            }
            R.id.textViewReviewCount -> {
                layoutAbout!!.isSelected = false
                textViewAbout!!.isSelected = false
                layoutReview!!.isSelected = true
                textViewReview!!.isSelected = true
                layoutAddPoint!!.isSelected = false
                scrollViewAbout!!.visibility = View.GONE
                recyclerViewReview!!.visibility = View.VISIBLE
                imgback!!.visibility = View.VISIBLE
            }
        }
    }

    override fun setProfileData(response: com.liftPlzz.model.createProfile.Response) {
        if (response.user != null) {
            userData = response.user
            val user = response.user
            editTextName!!.text = user.name
            if (!user.aboutMe.isEmpty()) {
                textViewAboutUser!!.text = user.aboutMe
            }
            profilePercentTxt!!.text = user.profile_percentage.toString() + " %"
            textViewMobile!!.text = user.mobile
            textViewEmail!!.text = user.email
            tvShareCode!!.text = "" + user.shareCode
            textViewRating!!.text = user.rating + "/5"
            textViewReviewCount!!.text = "(" + user.totalReview + " Reviews)"
            textViewTotalLift!!.text = user.totalLift.toString()
            textViewTakerLift!!.text = user.liftTaker.toString()
            textViewGiverLift!!.text = user.liftGiver.toString()
            imageslist = ArrayList()
            if (user.image != null && !user.image.isEmpty()) {
                val socialImage = SocialImage()
                socialImage.image = user.image
                imageslist?.add(socialImage)
            }
            if (user.socialImages.size > 0) {
                imageslist?.addAll(user.socialImages)
            }
            mViewPagerAdapter = ViewPagerAdapter(activity, imageslist, this@ProfileFragment)
            // Adding the Adapter to the ViewPager
            viewPagerMain!!.adapter = mViewPagerAdapter
            indicator!!.setViewPager(viewPagerMain)
        }
    }

    override fun setProfileImageData(message: String) {
        presenter!!.getProfile(sharedPreferences!!.getString(Constants.TOKEN, ""))
    }

    override fun setReviewData(data: List<Datum>) {
        recyclerViewReview!!.layoutManager = LinearLayoutManager(
            context
        )
        recyclerViewReview!!.adapter = ReviewListAdapter(context, data, this@ProfileFragment)
    }

    override fun onclick(s: Int) {}
    override fun onDeleteClick(s: Int) {
        presenter!!.delete_imag(sharedPreferences!!.getString(Constants.TOKEN, ""), "" + s)
    }

    override fun onEditClick(s: com.liftPlzz.model.getVehicle.Datum) {}
    override fun onAddImage() {
        with(this)
            .crop(1080f, 700f) //Crop image(Optional), Check Customization for more option
            .compress(1024) //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            ) //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.e("hdhdhd", "onActivityResult: " + data!!.data.toString())
            var file: File? = null
            try {
                file = File(URL(data.dataString).toURI())
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }
//            val requestFile = RequestBody.create(parse.parse("multipart/form-data"), file!!)
//            val body: Part = createFormData.createFormData("image", file.name, requestFile)
//            val api_key = RequestBody.create(MultipartBody.FORM, Constants.API_KEY)
//            val device = RequestBody.create(MultipartBody.FORM, "android")
//            val token = RequestBody.create(
//                MultipartBody.FORM, sharedPreferences!!.getString(
//                    Constants.TOKEN, ""
//                )!!
//            )
//            presenter!!.uploadImage(api_key, device, token, body)

            //File file= =ImagePicker.getFile(data);
        }
    }

    private fun getpoints() {
        Constants.showLoader(context)
        val queue = Volley.newRequestQueue(context)
        val sr: StringRequest = object : StringRequest(
            Method.POST,
            "https://charpair.com/api/my-balance",
            Response.Listener { response ->
                Constants.hideLoader()
                Log.d("getpointsresponse", response!!)
                try {
                    val jObject = JSONObject(response)
                    val points = jObject.getString("points")
                    val dialogBuilder = AlertDialog.Builder(
                        requireContext()
                    )
                    // ...Irrelevant code for customizing the buttons and title
                    val inflater = layoutInflater
                    dialogBuilder.setTitle("Available Points")
                    dialogBuilder.setCancelable(true)
                    dialogBuilder.setPositiveButton("Ok") { dialogInterface: DialogInterface?, i: Int -> }
                    val dialogView = inflater.inflate(R.layout.user_points_layout, null)
                    dialogBuilder.setView(dialogView)
                    val pointstext = dialogView.findViewById<TextView>(R.id.pointstextviewetxt)
                    pointstext.text = points
                    val alertDialog = dialogBuilder.create()
                    alertDialog.show()
                    dialogBuilder.setPositiveButton("Close") { dialogInterface: DialogInterface?, i: Int -> alertDialog.hide() }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { }) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["api_key"] = Constants.API_KEY
                params["client"] = Constants.ANDROID
                params["token"] = strToken!!
                //                params.put("token", "064ywr3Ht5LPpFPF73J0foCAdvw3ylSDXJys8IqATQ2wyvwimen827FAPA5I");
//                params.put("api_key", "070b92d28adc166b3a6c63c2d44535d2f62a3e24");
//                params.put("client", "android");
//                params.put("token", "NRy4MvEaDj5O04r8S6GGSZAJ7T5tv1QvS969rtgyYe7qdyKv8q6wjWBozH5I");
//                params.put("request_id", "57");
                return params
            }
        }
        queue.add(sr)
    }
}